package com.eu.habbo.messages.outgoing.gamcenter;

import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;

public class GameCenterGameListComposer extends MessageComposer
{
    @Override
    public ServerMessage compose()
    {
        this.response.init(Outgoing.GameCenterGameListComposer);
        this.response.appendInt(2);//Count

        this.response.appendInt(0);
        this.response.appendString("snowwar");
        this.response.appendString("93d4f3");
        this.response.appendString("");
        this.response.appendString("http://localhost/game/c_images/gamecenter_snowwar/");
        this.response.appendString("");

        this.response.appendInt(3);
        this.response.appendString("basejump");
        this.response.appendString("68bbd2"); //Background Color
        this.response.appendString(""); //Text color
        this.response.appendString("http://localhost/game/c_images/gamecenter_basejump/");
        this.response.appendString("");

        return this.response;
    }
}