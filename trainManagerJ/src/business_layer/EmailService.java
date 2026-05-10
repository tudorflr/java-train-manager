package business_layer;

import model.Booking;
import model.Train;

import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EmailService {
    private String host;
    private int port;
    private String username;
    private String password;
    private String fromEmail;
    private boolean useStartTls;
    private boolean useSsl;

    public EmailService() {
        host = System.getenv("SMTP_HOST");
        port = readPort();
        username = System.getenv("SMTP_USER");
        password = System.getenv("SMTP_PASSWORD");
        fromEmail = System.getenv("SMTP_FROM");
        useStartTls = !"false".equalsIgnoreCase(System.getenv("SMTP_STARTTLS"));
        useSsl = "true".equalsIgnoreCase(System.getenv("SMTP_SSL"));

        if(fromEmail == null || fromEmail.isBlank()) {
            fromEmail = username;
        }
    }

    public boolean sendBookingConfirmation(String email, Booking booking) {
        String subject = "your train booking";
        String body = "Hi, your booking is in\n\n" + booking + "\n";

        return sendEmail(email, subject, body);
    }

    public boolean sendDelayNotification(String email, Train train) {
        String subject = "train delay update";
        String body = "Heads up, train " + train.getTrainName() +
                " is running " + train.getDelayMinutes() + " min late\n";

        return sendEmail(email, subject, body);
    }

    private boolean sendEmail(String toEmail, String subject, String body) {
        if(!isConfigured()) {
            System.out.println("Email not sent, SMTP settings are missing");
            System.out.println("Set SMTP_HOST, SMTP_USER, SMTP_PASSWORD and optionally SMTP_PORT / SMTP_FROM");
            return false;
        }

        try {
            sendThroughSmtp(toEmail, subject, body);
            System.out.println("Email sent to " + toEmail);
            return true;
        } catch(IOException e) {
            System.out.println("Email could not be sent: " + e.getMessage());
            return false;
        }
    }

    private boolean isConfigured() {
        return host != null && !host.isBlank() &&
                username != null && !username.isBlank() &&
                password != null && !password.isBlank() &&
                fromEmail != null && !fromEmail.isBlank();
    }

    private int readPort() {
        String portText = System.getenv("SMTP_PORT");

        if(portText == null || portText.isBlank()) {
            return 587;
        }

        try {
            return Integer.parseInt(portText);
        } catch(NumberFormatException e) {
            return 587;
        }
    }

    private void sendThroughSmtp(String toEmail, String subject, String body) throws IOException {
        Socket socket;
        SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();

        if(useSsl) {
            socket = sslSocketFactory.createSocket(host, port);
        } else {
            socket = new Socket(host, port);
        }

        try {
            SmtpConnection smtp = new SmtpConnection(socket);
            smtp.expect(220);
            smtp.sendCommand("EHLO localhost", 250);

            if(useStartTls && !useSsl) {
                smtp.sendCommand("STARTTLS", 220);
                Socket tlsSocket = sslSocketFactory.createSocket(socket, host, port, true);
                socket = tlsSocket;
                smtp = new SmtpConnection(tlsSocket);
                smtp.sendCommand("EHLO localhost", 250);
            }

            smtp.sendCommand("AUTH LOGIN", 334);
            smtp.sendCommand(base64(username), 334);
            smtp.sendCommand(base64(password), 235);

            smtp.sendCommand("MAIL FROM:<" + fromEmail + ">", 250);
            smtp.sendCommand("RCPT TO:<" + toEmail + ">", 250, 251);
            smtp.sendCommand("DATA", 354);
            smtp.sendData(buildMessage(toEmail, subject, body));
            smtp.expect(250);
            smtp.sendCommand("QUIT", 221);
        } finally {
            socket.close();
        }
    }

    private String buildMessage(String toEmail, String subject, String body) {
        return "From: " + fromEmail + "\r\n" +
                "To: " + toEmail + "\r\n" +
                "Subject: " + subject + "\r\n" +
                "MIME-Version: 1.0\r\n" +
                "Content-Type: text/plain; charset=UTF-8\r\n" +
                "Content-Transfer-Encoding: 8bit\r\n" +
                "\r\n" +
                escapeMessageBody(body) +
                "\r\n.";
    }

    private String escapeMessageBody(String body) {
        String[] lines = body.split("\\R", -1);
        StringBuilder escaped = new StringBuilder();

        for(int i = 0; i < lines.length; i++) {
            if(lines[i].startsWith(".")) {
                escaped.append(".");
            }

            escaped.append(lines[i]);

            if(i < lines.length - 1) {
                escaped.append("\r\n");
            }
        }

        return escaped.toString();
    }

    private String base64(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    private static class SmtpConnection {
        private BufferedReader reader;
        private BufferedWriter writer;

        SmtpConnection(Socket socket) throws IOException {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        }

        void sendCommand(String command, int... acceptedCodes) throws IOException {
            writer.write(command + "\r\n");
            writer.flush();
            expect(acceptedCodes);
        }

        void sendData(String message) throws IOException {
            writer.write(message + "\r\n");
            writer.flush();
        }

        void expect(int... acceptedCodes) throws IOException {
            SmtpResponse response = readResponse();

            for(int code : acceptedCodes) {
                if(response.getCode() == code) {
                    return;
                }
            }

            throw new IOException(response.getText());
        }

        private SmtpResponse readResponse() throws IOException {
            String line = reader.readLine();

            if(line == null) {
                throw new IOException("No response from SMTP server");
            }

            StringBuilder responseText = new StringBuilder(line);
            int code = Integer.parseInt(line.substring(0, 3));

            while(line.length() > 3 && line.charAt(3) == '-') {
                line = reader.readLine();

                if(line == null) {
                    throw new IOException("SMTP response ended unexpectedly");
                }

                responseText.append(" ").append(line);
            }

            return new SmtpResponse(code, responseText.toString());
        }
    }

    private static class SmtpResponse {
        private int code;
        private String text;

        SmtpResponse(int code, String text) {
            this.code = code;
            this.text = text;
        }

        int getCode() {
            return code;
        }

        String getText() {
            return text;
        }
    }
}
