package com.restapi.scoregoat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Mail {
    private final String mailTo;
    private final String subject;
    private final String user;
    private final String newPassword;
    private final String toCcs;

    public static class MailBuilder {
        private String mailTo;
        private String subject;
        private String user;
        private String newPassword;
        private String toCcs;

        public MailBuilder mailTo(String mailTo) {
            this.mailTo = mailTo;
            return this;
        }

        public MailBuilder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public MailBuilder user(String user) {
            this.user = user;
            return this;
        }
        public MailBuilder newPassword(String newPassword) {
            this.newPassword = newPassword;
            return this;
        }

        public MailBuilder toCcs(String toCc) {
            if (this.toCcs == null) {
                this.toCcs = toCc;
            } else {
                this.toCcs += ", " + toCc;
            }
            return this;
        }

        public Mail build() {

            return new Mail(mailTo, subject, user, newPassword, toCcs);
        }
    }
}
