module dev.mccue.microhttp.jackson {
    requires transitive com.fasterxml.jackson.databind;
    requires transitive dev.mccue.microhttp.handler;
    requires transitive org.microhttp;

    requires dev.mccue.reasonphrase;

    exports dev.mccue.microhttp.jackson;
}