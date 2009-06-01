/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package com.ragnarson.ioke.couchdb;

import ioke.lang.DefaultArgumentsDefinition;
import ioke.lang.IokeObject;
import ioke.lang.NativeMethod;
import ioke.lang.Runtime;
import ioke.lang.Text;
import ioke.lang.exceptions.ControlFlow;
import ioke.lang.extensions.Extension;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
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

        database.registerMethod(runtime.newNativeMethod(Exists.DOC,
                new Exists()));
        database.registerMethod(runtime.newNativeMethod(Create.DOC,
                new Create()));
        database.registerMethod(runtime.newNativeMethod(Destroy.DOC,
                new Destroy()));
        database.registerMethod(runtime.newNativeMethod(CreateObject.DOC,
                new CreateObject()));
    }

    private IokeObject mimicOrigin(Runtime runtime, String kind,
            String description) {
        IokeObject mimic = new IokeObject(runtime, description);
        mimic.setKind(kind);
        mimic.mimicsWithoutCheck(runtime.origin);
        return mimic;
    }

    private class Create extends NativeMethod.WithNoArguments {
        public static final String DOC = "creates database";

        public Create() {
            super("create!");
        }

        @Override
        public Object activate(IokeObject method, IokeObject context,
                IokeObject message, Object on) throws ControlFlow {
            Runtime runtime = context.runtime;
            String url = Text.getText(IokeObject.perform((IokeObject)on, context, runtime.newMessage("url")));
            HttpClient client = new HttpClient();
            PutMethod httpMethod = new PutMethod(url);

            httpMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
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
                System.err.println("Fatal transport error: " + e.getMessage());
                e.printStackTrace();
            } finally {
                // Release the connection.
                httpMethod.releaseConnection();
            }

            return (statusCode == 200) ? runtime._true : runtime._false;
        }
    }

    private class Exists extends NativeMethod.WithNoArguments {
        public static final String DOC = "returns true if database exists, false otherwise";

        public Exists() {
            super("exists?");
        }

        @Override
        public Object activate(IokeObject method, IokeObject context,
                IokeObject message, Object on) throws ControlFlow {
            Runtime runtime = context.runtime;
            String url = Text.getText(IokeObject.perform((IokeObject)on, context, runtime.newMessage("url")));
            HttpClient client = new HttpClient();
            GetMethod httpMethod = new GetMethod(url);

            httpMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
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
                System.err.println("Fatal transport error: " + e.getMessage());
                e.printStackTrace();
            } finally {
                // Release the connection.
                httpMethod.releaseConnection();
            }

            return (statusCode == 200) ? runtime._true : runtime._false;
        }
    }

    private class Destroy extends NativeMethod.WithNoArguments {
        public static final String DOC = "destroys database and all it's data";

        public Destroy() {
            super("destroy!");
        }

        @Override
        public Object activate(IokeObject method, IokeObject context,
                IokeObject message, Object on) throws ControlFlow {
            Runtime runtime = context.runtime;
            String url = Text.getText(IokeObject.perform((IokeObject)on, context, runtime.newMessage("url")));
            HttpClient client = new HttpClient();
            DeleteMethod httpMethod = new DeleteMethod(url);

            httpMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
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
                System.err.println("Fatal transport error: " + e.getMessage());
                e.printStackTrace();
            } finally {
                // Release the connection.
                httpMethod.releaseConnection();
            }

            return (statusCode == 200) ? runtime._true : runtime._false;
        }
    }

    private static class CreateObject extends NativeMethod {
        public static final String DOC = "creates database entry for given JSON representation";

        public static final DefaultArgumentsDefinition ARGUMENTS = DefaultArgumentsDefinition
                .builder().withRequiredPositional("object").getArguments();

        @Override
        public DefaultArgumentsDefinition getArguments() {
            return ARGUMENTS;
        }

        public CreateObject() {
            super("createObject");
        }

        @Override
        public Object activate(IokeObject method, IokeObject context,
                IokeObject message, Object on) throws ControlFlow {
            List<Object> args = new ArrayList<Object>();
            getArguments().getEvaluatedArguments(context, message, on, args,
                    new HashMap<String, Object>());
            Runtime runtime = context.runtime;
            String url = Text.getText(IokeObject.perform((IokeObject)on, context, runtime.newMessage("url")));
            IokeObject object = (IokeObject)args.get(0);
            String jsonRepresentation = Text.getText(IokeObject.perform((IokeObject)object, context, runtime.newMessage("toJson")));
            HttpClient client = new HttpClient();
            PutMethod put = new PutMethod(url);

            put.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                    new DefaultHttpMethodRetryHandler(3, false));
            try {
                put.setRequestEntity(new StringRequestEntity(
                        jsonRepresentation, "application/json", "utf-8"));
            } catch (UnsupportedEncodingException e1) {
            }

            int statusCode = 0;

            try {
                // Execute the method.
                statusCode = client.executeMethod(put);
            } catch (HttpException e) {
                System.err.println("Fatal protocol violation: "
                        + e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println("Fatal transport error: " + e.getMessage());
                e.printStackTrace();
            } finally {
                // Release the connection.
                put.releaseConnection();
            }

            return (statusCode < 300) ? runtime._true : runtime._false;
        }

    }
}
