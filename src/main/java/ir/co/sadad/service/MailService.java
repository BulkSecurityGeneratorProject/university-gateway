package ir.co.sadad.service;

import ir.co.sadad.domain.User;
import ir.co.sadad.mail.MailEvent;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

/**
 * Service for sending e-mails.
 * <p>
 * CDI events is used to send e-mails asynchronously.
 * </p>
 */
@ApplicationScoped
public class MailService {

    @Inject
    private Logger log;

    @Inject
    private Event<MailEvent> eventProducer;

    public void sendActivationEmail(User user) {
        log.debug("Sending activation e-mail to '{}'", user.getEmail());
        eventProducer.fireAsync(new MailEvent(user, "email.activation.title", "activationEmail"));
    }

    public void sendCreationEmail(User user) {
        log.debug("Sending creation e-mail to '{}'", user.getEmail());
        eventProducer.fireAsync(new MailEvent(user, "email.creation.title", "creationEmail"));
    }

    public void sendPasswordResetMail(User user) {
        log.debug("Sending password reset e-mail to '{}'", user.getEmail());
        eventProducer.fireAsync(new MailEvent(user, "email.reset.title", "passwordResetEmail"));
    }

}
