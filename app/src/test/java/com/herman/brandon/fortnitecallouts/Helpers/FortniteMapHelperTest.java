package com.herman.brandon.fortnitecallouts.Helpers;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class FortniteMapHelperTest {

    @Test
    public void parseVoiceCommandResultsTest() {
        List<String> t1 = Arrays.asList("A1 to B2", "a1 to b2");

        assertThat(FortniteMapHelper.parseVoiceCommandResults(t1), is(FortniteMapHelper.calculateTravelTime(0,0, 1, 1)));
    }
}