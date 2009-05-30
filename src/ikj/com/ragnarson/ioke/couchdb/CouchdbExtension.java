/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package com.ragnarson.ioke.couchdb;

import java.io.IOException;

import ioke.lang.exceptions.ControlFlow;

import ioke.lang.DefaultArgumentsDefinition;
import ioke.lang.IokeObject;
import ioke.lang.NativeMethod;
import ioke.lang.Runtime;
import ioke.lang.Text;
import ioke.lang.extensions.Extension;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;

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
		IokeObject couchDB = mimicOrigin(runtime, "CouchDB",
				"CouchDB provides access to CouchDB instance");
		IokeObject database = mimicOrigin(runtime, "CouchDB Database",
				"Represents a single CouchDB instance");
		runtime.ground.setCell("CouchDB", couchDB);
		couchDB.setCell("Database", database);

		database.registerMethod(runtime.newNativeMethod(
				"returns true if database exists", existsMethod(database)));
		database.registerMethod(runtime.newNativeMethod(
				"creates database", createMethod(database)));
		database.registerMethod(runtime.newNativeMethod(
				"destroys database and all it's data", destroyMethod(database)));
	}

	private IokeObject mimicOrigin(Runtime runtime, String kind,
			String description) {
		IokeObject mimic = new IokeObject(runtime, description);
		mimic.setKind(kind);
		mimic.mimicsWithoutCheck(runtime.origin);
		return mimic;
	}

	private NativeMethod existsMethod(IokeObject database) {
		return new NativeMethod.WithNoArguments("exists?") {
			@Override
			public Object activate(IokeObject method, IokeObject context,
					IokeObject message, Object on) throws ControlFlow {
				Runtime runtime = context.runtime;
				String url = Text.getText(IokeObject.getCell(on, message,
						context, "url"));
				HttpClient client = new HttpClient();
				GetMethod httpMethod = new GetMethod(url);

				httpMethod.getParams().setParameter(
						HttpMethodParams.RETRY_HANDLER,
						new DefaultHttpMethodRetryHandler(3, false));
				int statusCode = 0;

				try {
					// Execute the method.
					statusCode = client.executeMethod(httpMethod);
				} catch (HttpException e) {
					System.err.println("Fatal protocol violation: "
							+ e.getMessage());
					e.printStackTrace();
				} catch (IOException e) {
					System.err.println("Fatal transport error: "
							+ e.getMessage());
					e.printStackTrace();
				} finally {
					// Release the connection.
					httpMethod.releaseConnection();
				}

				return (statusCode == 200) ? runtime._true : runtime._false;
			}
		};
	}

	private NativeMethod createMethod(IokeObject database) {
		return new NativeMethod.WithNoArguments("create!") {
			@Override
			public Object activate(IokeObject method, IokeObject context,
					IokeObject message, Object on) throws ControlFlow {
				Runtime runtime = context.runtime;
				String url = Text.getText(IokeObject.getCell(on, message,
						context, "url"));
				HttpClient client = new HttpClient();
				PutMethod httpMethod = new PutMethod(url);

				httpMethod.getParams().setParameter(
						HttpMethodParams.RETRY_HANDLER,
						new DefaultHttpMethodRetryHandler(3, false));
				int statusCode = 0;

				try {
					// Execute the method.
					statusCode = client.executeMethod(httpMethod);
				} catch (HttpException e) {
					System.err.println("Fatal protocol violation: "
							+ e.getMessage());
					e.printStackTrace();
				} catch (IOException e) {
					System.err.println("Fatal transport error: "
							+ e.getMessage());
					e.printStackTrace();
				} finally {
					// Release the connection.
					httpMethod.releaseConnection();
				}

				return (statusCode == 200) ? runtime._true : runtime._false;
			}
		};
	}

	private NativeMethod destroyMethod(IokeObject database) {
		return new NativeMethod.WithNoArguments("destroy!") {
			@Override
			public Object activate(IokeObject method, IokeObject context,
					IokeObject message, Object on) throws ControlFlow {
				Runtime runtime = context.runtime;
				String url = Text.getText(IokeObject.getCell(on, message,
						context, "url"));
				HttpClient client = new HttpClient();
				DeleteMethod httpMethod = new DeleteMethod(url);

				httpMethod.getParams().setParameter(
						HttpMethodParams.RETRY_HANDLER,
						new DefaultHttpMethodRetryHandler(3, false));
				int statusCode = 0;

				try {
					// Execute the method.
					statusCode = client.executeMethod(httpMethod);
				} catch (HttpException e) {
					System.err.println("Fatal protocol violation: "
							+ e.getMessage());
					e.printStackTrace();
				} catch (IOException e) {
					System.err.println("Fatal transport error: "
							+ e.getMessage());
					e.printStackTrace();
				} finally {
					// Release the connection.
					httpMethod.releaseConnection();
				}

				return (statusCode == 200) ? runtime._true : runtime._false;
			}
		};
	}
}