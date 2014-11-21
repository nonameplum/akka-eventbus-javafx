package pl.plum.javafx;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.UntypedActor;
import akka.event.japi.LookupEventBus;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class AkkaEventBusImpl {

    /**
     * Way to send events to multiple subscribers. Based on Akka {@link akka.event.japi.LookupEventBus}.
     */
    public class EventBus extends LookupEventBus<AkkaEvent, ActorRef, String> {

        @Override
        public int mapSize() {
            return 128;
        }

        @Override
        public int compareSubscribers(ActorRef a, ActorRef b) {
            return a.compareTo(b);
        }

        @Override
        public String classify(AkkaEvent event) {
            return event.getChannel();
        }

        @Override
        public void publish(AkkaEvent event, ActorRef subscriber) {
            subscriber.tell(event, ActorRef.noSender());
        }
    }

    /**
     * This is the Actor implementation. i.e. The object that is being subscribed to listen for events.
     * aka: Observer.
     */
    public static class AkkaEventHandler extends UntypedActor {

        private javafx.event.EventHandler eventHandler = null;

        public AkkaEventHandler(javafx.event.EventHandler eventHandler) {
            this.eventHandler = eventHandler;
        }

        /**
         * On receive message called by akka, event handler method is run.
         * Receive callback is wrapped in JavaFx runLater method to synchronize with JavaFx thread from akka context.
         * @param message
         */
        @Override
        public void onReceive(final Object message) {
            Platform.runLater(() -> {
                if ((eventHandler != null) && (message instanceof AkkaEvent)) {
                    eventHandler.handle((AkkaEvent) message);
                }
            });
        }
    }

    public final EventBus eventBus;
    public final ActorSystem actorSystem;
    public final Stage primaryStage;

    public AkkaEventBusImpl(Stage primaryStage) {
        this.eventBus = new EventBus();
        actorSystem = ActorSystem.create("Events");

        this.primaryStage = primaryStage;
        primaryStage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
            AkkaEventBus.getActorSystem().shutdown();
        });
    }

    public void subscribe(ActorRef subscriber, String to) {
        eventBus.subscribe(subscriber, to);
    }

    public void publish(AkkaEvent event) {
        eventBus.publish(event);
    }

    public void publish(AkkaEvent event, ActorRef subscriber) {
        eventBus.publish(event, subscriber);
    }

}
