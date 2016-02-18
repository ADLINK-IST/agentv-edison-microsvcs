package com.prismtech.edison.display.types;


/**
* com/prismtech/edison/display/types/LCDText.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ../idl//display.idl
* Tuesday, February 16, 2016 1:52:37 AM CET
*/

/**
* Updated by idl2j
* from ../idl//display.idl
* Tuesday, February 16, 2016 1:52:37 AM CET
*/

import com.prismtech.cafe.dcps.keys.KeyList;

@KeyList(
    topicType = "LCDText",
    keys = {"id"}
)
public final class LCDText implements org.omg.CORBA.portable.IDLEntity
{
  public short id = (short)0;
  public short row = (short)0;
  public short col = (short)0;
  public String text = null;

  public LCDText ()
  {
  } // ctor

  public LCDText (short _id, short _row, short _col, String _text)
  {
    id = _id;
    row = _row;
    col = _col;
    text = _text;
  } // ctor

} // class LCDText
