module msgparser {
    requires commons.validator;
    requires org.apache.commons.lang3;
    requires org.apache.logging.log4j;
    requires org.apache.poi.poi;
    requires org.apache.poi.scratchpad;

    exports com.auxilii.msgparser;
    exports com.auxilii.msgparser.attachment;
}