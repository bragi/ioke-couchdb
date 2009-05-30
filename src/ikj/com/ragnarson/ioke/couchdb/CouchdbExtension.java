/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package com.ragnarson.ioke.couchdb;

import ioke.lang.exceptions.ControlFlow;

import ioke.lang.IokeObject;
import ioke.lang.NativeMethod;
import ioke.lang.Runtime;
import ioke.lang.Text;
import ioke.lang.extensions.Extension;

/**
 * Adds native methods needed to support JSON parsing and serialization to Ioke.
 *
 * author: <a href="mailto:bragi@ragnarson.com">Łukasz Piestrzeniewicz</a>
 */
public class CouchdbExtension extends Extension {
  public void Extension() {
    
  }

  @Override
  public void init(Runtime runtime) throws ControlFlow {
	IokeObject couchDB = mimicOrigin(runtime, "CouchDB", "CouchDB provides access to CouchDB instance");
	IokeObject database = mimicOrigin(runtime, "CouchDB Database", "Represents a single CouchDB instance");
	runtime.ground.setCell("CouchDB", couchDB);
	couchDB.setCell("Database", database);
  }
  
  private IokeObject mimicOrigin(Runtime runtime, String kind, String description) {
	  IokeObject mimic = new IokeObject(runtime, description);
	  mimic.setKind(kind);
	  mimic.mimicsWithoutCheck(runtime.origin);
	  return mimic;
  }
}