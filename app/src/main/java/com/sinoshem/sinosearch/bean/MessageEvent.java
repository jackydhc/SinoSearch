package com.sinoshem.sinosearch.bean;

/**
 * @author jackydu
 * @date 2018/8/17
 */
public class MessageEvent {
    public int MessageEventCode;
    public Object values;
    public String msg;

    public MessageEvent() {
    }

    public MessageEvent(int messageEventCode) {
        MessageEventCode = messageEventCode;
    }

    public MessageEvent(int messageEventCode, Object values) {
        MessageEventCode = messageEventCode;
        this.values = values;
    }

    public MessageEvent(int messageEventCode, Object values, String msg) {
        MessageEventCode = messageEventCode;
        this.values = values;
        this.msg = msg;
    }
}
