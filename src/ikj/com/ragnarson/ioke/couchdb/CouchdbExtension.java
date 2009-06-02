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
import org.apache.commons.httpclient.methods.PostMethod;
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
        IokeObject resource = mimicOrigin(runtime, "Resource",
                "represents a HTTP resource with given URL and representation");
        runtime.ground.setCell("Resource", resource);
        
        resource.registerMethod(runtime.newNativeMethod(Get.DOC,
                new Get()));
        resource.registerMethod(runtime.newNativeMethod(Put.DOC,
                new Put()));
        resource.registerMethod(runtime.newNativeMethod(Delete.DOC,
                new Delete()));
        resource.registerMethod(runtime.newNativeMethod(Post.DOC,
                new Post()));
    }

    private IokeObject mimicOrigin(Runtime runtime, String kind,
            String description) {
        IokeObject mimic = new IokeObject(runtime, description);
        mimic.setKind(kind);
        mimic.mimicsWithoutCheck(runtime.origin);
        return mimic;
    }

    private class Get extends NativeMethod.WithNoArguments {
        public static final String DOC = "executes GET request on resource";

        public Get() {
            super("get");
        }

        @Override
        public Object activate(IokeObject method, IokeObject context,
                IokeObject message, Object on) throws ControlFlow {
            Runtime runtime = context.runtime;
            IokeObject resource = (IokeObject)on;
            String url = Text.getText(IokeObject.perform(resource, context, runtime.newMessage("url")));
            HttpClient client = new HttpClient();
            GetMethod httpMethod = new GetMethod(url);

            httpMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                    new DefaultHttpMethodRetryHandler(3, false));
            int statusCode = 0;
            String representation = "";

            try {
                // Execute the method.
                statusCode = client.executeMethod(httpMethod);
                representation = httpMethod.getResponseBodyAsString();
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
            
            resource.setCell("result", runtime.newNumber(statusCode));
            resource.setCell("representation", runtime.newText(representation));

            return resource;
        }
    }

    private class Put extends NativeMethod.WithNoArguments {
        public static final String DOC = "executes PUT request on resource";

        public Put() {
            super("put");
        }

        @Override
        public Object activate(IokeObject method, IokeObject context,
                IokeObject message, Object on) throws ControlFlow {
            Runtime runtime = context.runtime;
            IokeObject resource = (IokeObject)on;
            String url = Text.getText(IokeObject.perform(resource, context, runtime.newMessage("url")));
            HttpClient client = new HttpClient();
            PutMethod httpMethod = new PutMethod(url);

            httpMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                    new DefaultHttpMethodRetryHandler(3, false));
            int statusCode = 0;
            String representation = Text.getText(IokeObject.perform(resource, context, runtime.newMessage("representation")));
            String mimeType = Text.getText(IokeObject.perform(resource, context, runtime.newMessage("mimeType")));
            
            try {
                httpMethod.setRequestEntity(new StringRequestEntity(
                        representation, mimeType, "utf-8"));
            } catch (UnsupportedEncodingException e1) {
            }

            try {
                // Execute the method.
                statusCode = client.executeMethod(httpMethod);
                representation = httpMethod.getResponseBodyAsString();
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

            resource.setCell("result", runtime.newNumber(statusCode));
            resource.setCell("representation", runtime.newText(representation));

            return resource;
        }
    }

    private class Delete extends NativeMethod.WithNoArguments {
        public static final String DOC = "executes DELETE request on resource";

        public Delete() {
            super("delete");
        }

        @Override
        public Object activate(IokeObject method, IokeObject context,
                IokeObject message, Object on) throws ControlFlow {
            Runtime runtime = context.runtime;
            IokeObject resource = (IokeObject)on;
            String url = Text.getText(IokeObject.perform(resource, context, runtime.newMessage("url")));
            HttpClient client = new HttpClient();
            DeleteMethod httpMethod = new DeleteMethod(url);

            httpMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                    new DefaultHttpMethodRetryHandler(3, false));
            int statusCode = 0;
            String representation = "";

            try {
                // Execute the method.
                statusCode = client.executeMethod(httpMethod);
                representation = httpMethod.getResponseBodyAsString();
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

            resource.setCell("result", runtime.newNumber(statusCode));
            resource.setCell("representation", runtime.newText(representation));

            return resource;
        }
    }

    private class Post extends NativeMethod.WithNoArguments {
        public static final String DOC = "executes POST request on resource";

        public Post() {
            super("post");
        }

        @Override
        public Object activate(IokeObject method, IokeObject context,
                IokeObject message, Object on) throws ControlFlow {
            Runtime runtime = context.runtime;
            IokeObject resource = (IokeObject)on;
            String url = Text.getText(IokeObject.perform(resource, context, runtime.newMessage("url")));
            HttpClient client = new HttpClient();
            PostMethod httpMethod = new PostMethod(url);

            httpMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                    new DefaultHttpMethodRetryHandler(3, false));
            int statusCode = 0;
            String representation = Text.getText(IokeObject.perform(resource, context, runtime.newMessage("representation")));
            String mimeType = Text.getText(IokeObject.perform(resource, context, runtime.newMessage("mimeType")));
            
            try {
                httpMethod.setRequestEntity(new StringRequestEntity(
                        representation, mimeType, "utf-8"));
            } catch (UnsupportedEncodingException e1) {
            }

            try {
                // Execute the method.
                statusCode = client.executeMethod(httpMethod);
                representation = httpMethod.getResponseBodyAsString();
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

            resource.setCell("result", runtime.newNumber(statusCode));
            resource.setCell("representation", runtime.newText(representation));

            return resource;
        }
    }

}
