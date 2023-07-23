module msgparser {
    requires commons.validator;
    requires org.apache.commons.lang3;
    requires org.apache.logging.log4j;
    requires org.apache.commons.io;
    requires transitive org.apache.poi.scratchpad;

    exports com.auxilii.msgparser;
    exports com.auxilii.msgparser.attachment;
}