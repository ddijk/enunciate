package org.codehaus.enunciate.examples.c.schema.structures;

import com.webcohesion.enunciate.metadata.qname.XmlQNameEnum;
import com.webcohesion.enunciate.metadata.qname.XmlUnknownQNameEnumValue;

/**
 * @author Ryan Heaton
 */
@XmlQNameEnum
public enum HouseType {

  brick,

  wood,

  mud,

  @XmlUnknownQNameEnumValue
  unknown
}