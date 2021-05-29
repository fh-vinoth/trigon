package com.trigon.constants;

public class Message {
    public static final String ELEMENT_NOT_FOUND = "Element Not Found in the page/screen !! Check TestCase Flow and Previous Step Once : ";
    public static final String TIME_TAKEN_TO_IDENTIFY_ELEMENT = "Time Taken to Identify a element : ";
    public static final String TIME_TAKEN_TO_PERFORM_ACTION_ELEMENT = "Time Taken to Perform Action to a element : ";
    public static final String ELEMENT_WAIT_0 = "Element Wait Configured as 0: Recheck wait if it is intended as 0 or not";
    public static final String LOCATOR_NOT_AVAILABLE = "The given Locator is not available : ";
    public static final String CLICKED = "Clicked on the Button : ";
    public static final String ELEMENT_NOT_INTRACTABLE = "Element is Visible, but it is NOT intractable ";
    public static final String ELEMENT_NOT_INTRACTABLE_CLICK = ELEMENT_NOT_INTRACTABLE + "or NOT Clickable ";
    public static final String GET_ATTRIBUTE = "Captured the Attribute For : ";
    public static final String ELEMENT_NOT_INTRACTABLE_GET_ATTRIBUTE = ELEMENT_NOT_INTRACTABLE + "to capture Attribute ";
    public static final String ELEMENT_VERIFY_DISPLAYED = "Element is Displayed : ";
    public static final String ELEMENT_NOT_INTRACTABLE_VERIFY_DISPLAYED = ELEMENT_NOT_INTRACTABLE + "to check element displayed ";
    public static final String CLEAR_TEXT = "Cleared the Text : ";
    public static final String ELEMENT_NOT_INTRACTABLE_CLEAR_TEXT = ELEMENT_NOT_INTRACTABLE + "to Clear Text ";
    public static final String ELEMENT_NOT_INTRACTABLE_ENTER_TEXT = ELEMENT_NOT_INTRACTABLE + "to Enter Text ";
    public static final String ELEMENT_NOT_INTRACTABLE_GET_TEXT = ELEMENT_NOT_INTRACTABLE + "to Capture Text ";

    private Message() {
    }
}
