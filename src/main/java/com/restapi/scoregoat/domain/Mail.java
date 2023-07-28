package com.restapi.scoregoat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Mail {
    private final String mailTo;
    private final String subject;
    private final String message;
    private final String toCcs;

    public static class MailBuilder {
        private String mailTo;
        private String subject;
        private String message;
        private String toCcs;

        public MailBuilder mailTo(String mailTo) {
            this.mailTo = mailTo;
            return this;
        }

        public MailBuilder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public MailBuilder message(String message) {
            this.message = message;
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

            return new Mail(mailTo, subject, message, toCcs);
        }
    }
}
