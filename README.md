# BulkMailer

A command line based mail sender script to distribute to many addresses

## Build
```
mvn install
```

## Usage
```
usage: java -jar BulkMailer.jar -f <file path> [-h] -l <file path> 
       -m <mail address> -p <password> [-s <hostname>] [-t <title>] -u <username>
  -f <file path>      Mail message file
  -h                  show Help
  -l <file path>      Mail address list
  -m <mail address>   Your mail address
  -p <password>       IMAP password
  -s <hostname>       SMTP server (default: smtp.spmode.ne.jp)
  -t <title>          Subject (default: メールアドレス変更のご連絡)
  -u <username>       IMAP username
```

Mail address list (-l) should have one mail address in one line such like this.

```
friend1@example.com
friend2@example.com
friend3@example.com
```

Mail message file (-f) encoding is expected UTF-8.

### Use case
Using docomo mail

```
java -jar BulkMailer.jar -f myMessage.txt -l friendsList.txt 
                  -m mymail@docomo.ne.jp -u myDocomoId -p myDocomoPassword 
```
## misc

- Motivation is to send a mail using Docomo Mail
 
## License
MIT