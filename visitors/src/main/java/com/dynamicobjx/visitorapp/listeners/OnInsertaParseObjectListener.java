package com.dynamicobjx.visitorapp.listeners;


public interface OnInsertaParseObjectListener {
    public void onPreInsert();
    public void onInserted(Exception e);
    public void onInsertFinish();
}
