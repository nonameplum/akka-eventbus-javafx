package pl.plum.javafx;

import javafx.event.Event;
import javafx.event.EventType;

public class AkkaEvent<T> extends Event {
    public static final EventType<AkkaEvent> ANY =
            new EventType<AkkaEvent>(Event.ANY, "AKKA_EVENT");
    public static final EventType<AkkaEvent> AKKA_EVENT_MESSAGE =
            new EventType<AkkaEvent>(AkkaEvent.ANY, "AKKA_EVENT_MESSAGE");

    private String channel;
    private T message;

    public AkkaEvent(final String channel) {
        super(AkkaEvent.AKKA_EVENT_MESSAGE);
        this.channel = channel;
    }

    public AkkaEvent(final String channel, T message) {
        super(AkkaEvent.AKKA_EVENT_MESSAGE);

        this.channel = channel;
        this.message = message;
    }

    public String getChannel() {
        return channel;
    }

    public T getMessage() {
        return message;
    }
}
