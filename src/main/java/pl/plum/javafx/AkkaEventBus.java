package pl.plum.javafx;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import javafx.event.EventHandler;
import javafx.stage.Stage;

public enum AkkaEventBus {

    INSTANCE;

    public AkkaEventBusImpl akkaEventBusImpl;

    private void build(SingletonBuilder builder) {
        this.akkaEventBusImpl = builder.akkaEventBusImpl;
    }

    public static AkkaEventBusImpl getAkkaEventBus() {
        return INSTANCE.akkaEventBusImpl;
    }

    public static ActorSystem getActorSystem() {
        return INSTANCE.akkaEventBusImpl.actorSystem;
    }

    public static AkkaEventBusImpl.EventBus getEventBus() {
        return INSTANCE.akkaEventBusImpl.eventBus;
    }

    public static ActorRef actorOf(EventHandler eventHandler) {
        return getActorSystem().actorOf(Props.create(AkkaEventBusImpl.AkkaEventHandler.class, eventHandler));
    }

    public static void subscribeActor(ActorRef actor, String channel) {
        getAkkaEventBus().subscribe(actor, channel);
    }

    public static <T extends AkkaEvent>  void subscribeEventHandler(String channel, EventHandler<T> eventHandler) {
        final ActorRef actorRef = getActorSystem().actorOf(Props.create(AkkaEventBusImpl.AkkaEventHandler.class, eventHandler));
        getAkkaEventBus().subscribe(actorRef, channel);
    }

    public static <T extends AkkaEvent>  void subscribeEventHandler(String channel, EventHandler<T> eventHandler, Class<?> customAkkaEventHandler) {
        final ActorRef actorRef = getActorSystem().actorOf(Props.create(customAkkaEventHandler, eventHandler));
        getAkkaEventBus().subscribe(actorRef, channel);
    }

    public static void publish(AkkaEvent event) {
        getAkkaEventBus().publish(event);
    }

    public static class SingletonBuilder {

        private final AkkaEventBusImpl akkaEventBusImpl;

        public SingletonBuilder(Stage primaryStage){
            akkaEventBusImpl = new AkkaEventBusImpl(primaryStage);
        }

        public void build(){
            AkkaEventBus.INSTANCE.build(this);
        }

    }
}
