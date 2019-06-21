package com.liy.chat.netty.pojo.msgenum;

import org.junit.Assert;
import org.junit.Test;

public class RequestActionEnumTest {

    @Test
    public void getType() {
        RequestActionEnum action = RequestActionEnum.AGREE;
        Integer expectAction = 1;
        Integer actualAction = action.getType();

        Assert.assertEquals(expectAction, actualAction);
    }
}