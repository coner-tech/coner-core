package org.coner.core.util.merger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.coner.core.util.merger.ReflectionPayloadJavaBeanMerger.javaBeanToPayload;
import static org.coner.core.util.merger.ReflectionPayloadJavaBeanMerger.payloadToJavaBean;

import org.junit.Test;

public class ReflectionPayloadJavaBeanMergerTest {

    @Test
    public void whenMergeSimpleJavaBeanToSimplePayloadItShouldMerge() {
        SimpleJavaBean sourceJavaBean = new SimpleJavaBean();
        sourceJavaBean.setTheInt(42);

        SimplePayload destinationPayload = new SimplePayload();

        ReflectionPayloadJavaBeanMerger<SimpleJavaBean, SimplePayload> merger = javaBeanToPayload();
        merger.merge(sourceJavaBean, destinationPayload);

        assertThat(destinationPayload.theInt).isEqualTo(42);
    }

    @Test
    public void whenMergeSimplePayloadToSimpleJavaBeanItShouldMerge() {
        SimplePayload sourcePayload = new SimplePayload();
        sourcePayload.theInt = 42;

        SimpleJavaBean destinationJavaBean = new SimpleJavaBean();

        ReflectionPayloadJavaBeanMerger<SimplePayload, SimpleJavaBean> merger = payloadToJavaBean();
        merger.merge(sourcePayload, destinationJavaBean);

        assertThat(destinationJavaBean.getTheInt()).isEqualTo(42);
    }

    @Test
    public void whenMergeSimplePayloadToSlightlyDeviousJavaBeanItShouldMerge() {
        SimplePayload sourcePayload = new SimplePayload();
        sourcePayload.devious = true;
        SlightlyDeviousJavaBean destinationJavaBean = new SlightlyDeviousJavaBean();

        ReflectionPayloadJavaBeanMerger merger = payloadToJavaBean();
        merger.merge(sourcePayload, destinationJavaBean);

        assertThat(destinationJavaBean.getDevious()).isTrue();
    }

    public static class SimpleJavaBean {
        private int theInt;

        public int getTheInt() {
            return theInt;
        }

        public void setTheInt(int theInt) {
            this.theInt = theInt;
        }
    }

    public static class SimplePayload {
        public int theInt;
        public boolean devious;
    }

    public static class SlightlyDeviousJavaBean {
        private int theInt;
        private String devious;

        public int getTheInt() {
            return theInt;
        }

        public void setTheInt(int theInt) {
            this.theInt = theInt;
        }

        // return type doesn't match field type
        public boolean getDevious() {
            return Boolean.valueOf(devious);
        }

        // field type doesn't match parameter 0 type
        public void setDevious(boolean devious) {
            this.devious = String.valueOf(devious);
        }
    }
}
