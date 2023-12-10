module msgparser {
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    requires org.apache.commons.validator;
    requires org.apache.logging.log4j;
    requires transitive org.apache.poi.scratchpad;

    exports com.auxilii.msgparser;
    exports com.auxilii.msgparser.attachment;
}