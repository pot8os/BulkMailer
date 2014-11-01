package net.pot8os.bulkmailer;

import java.io.IOException;
import java.util.Scanner;
import javax.mail.MessagingException;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author pot8os
 */
public class Main {

    private final Options ops = new Options();

    public Main() {
        ops.addOption(new MyOpBuilder().withDescription("show Help").create("h"));
        ops.addOption(new MyOpBuilder().isRequired().hasOptionalArg()
                .withDescription("IMAP username").withArgName("username").create("u"));
        ops.addOption(new MyOpBuilder().isRequired().hasOptionalArg()
                .withDescription("IMAP password").withArgName("password").create("p"));
        ops.addOption(new MyOpBuilder().isRequired().hasOptionalArg()
                .withDescription("Mail message file").withArgName("file path").create("f"));
        ops.addOption(new MyOpBuilder().isRequired().hasOptionalArg()
                .withDescription("Mail address list").withArgName("file path").create("l"));
        ops.addOption(new MyOpBuilder().isRequired().hasOptionalArg()
                .withDescription("Your mail address").withArgName("mail address").create("m"));
        ops.addOption(new MyOpBuilder().hasOptionalArg()
                .withDescription("SMTP server (default: smtp.spmode.ne.jp)").withArgName("hostname").create("s"));
        ops.addOption(new MyOpBuilder().hasOptionalArg()
                .withDescription("Subject (default: メールアドレス変更のご連絡)").withArgName("title").create("t"));
    }

    public void prepare(String... args) throws MessagingException, IOException {
        try {
            final CommandLine line = new BasicParser().parse(ops, args);
            if (line.hasOption("h")) {
                showHelp();
                return;
            }
            final MailSender sender = new MailSender(line.getOptionValue("l"),
                    line.getOptionValue("u"), line.getOptionValue("p"), line.getOptionValue("m"),line.getOptionValue("f"));
            if (line.hasOption("s")) {
                sender.setSmtpHost(line.getOptionValue("s"));
            }
            if (line.hasOption("t")) {
                sender.setTitle(line.getOptionValue("t"));
            }
            while (sender.prepare()) {
                System.out.println("\n\nReady to send a mail. OK? [y/N]: ");
                Scanner scanner = new Scanner(System.in);
                if (scanner.nextLine().equalsIgnoreCase("y")) {
                    sender.send();
                } else {
                    System.out.println("Exit.");
                    return;
                }
            }
            System.out.println("Sent all mails. Bye!");
        } catch (ParseException ex) {
            System.err.print(ex);
            showHelp();
        }
    }

    private void showHelp() {
        new HelpFormatter().printHelp("java -jar BulkMailer.jar", ops, true);
    }

    public static void main(String... args) throws MessagingException, IOException {
        new Main().prepare(args);
    }
}
