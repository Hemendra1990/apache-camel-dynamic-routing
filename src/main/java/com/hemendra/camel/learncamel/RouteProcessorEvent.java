package com.hemendra.camel.learncamel;

import java.io.Serializable;

public class RouteProcessorEvent implements Serializable {

        private Object message;

        public RouteProcessorEvent(Object message) {
            this.message = message;
        }

    public Object getMessage() {
            return message;
        }

        public void setMessage(Object message) {
            this.message = message;
        }
}
