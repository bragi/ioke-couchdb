package com.ragnarson.ioke.couchdb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;

import ioke.lang.IokeObject;
import ioke.lang.Runtime;
import ioke.lang.Text;
import ioke.lang.NativeMethod.WithNoArguments;
import ioke.lang.exceptions.ControlFlow;

public abstract class ResourceMethod extends WithNoArguments {

	public ResourceMethod(String name) {
		super(name);
	}
	
	public abstract HttpMethod getHttpMethod(String url);

	@Override
    public Object activate(IokeObject method, IokeObject context,
            IokeObject message, Object on) throws ControlFlow {
        Runtime runtime = context.runtime;
        IokeObject resource = (IokeObject)on;
        String url = Text.getText(IokeObject.perform(resource, context, runtime.newMessage("url")));
        HttpClient client = new HttpClient();
        HttpMethod httpMethod = getHttpMethod(url);

        httpMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));
        int statusCode = 0;
        String stringRepresentation = "";
        Object representation = IokeObject.perform(resource, context, runtime.newMessage("representation"));
        String mimeType = Text.getText(IokeObject.perform(resource, context, runtime.newMessage("mimeType")));
        
        try {
        	if(httpMethod instanceof EntityEnclosingMethod) {
            	if(representation != runtime.nil) {
                    ((EntityEnclosingMethod)httpMethod).setRequestEntity(new StringRequestEntity(
                    		Text.getText(representation), mimeType, "utf-8"));
            	}
        	}
        } catch (UnsupportedEncodingException e1) {
        }

        try {
            // Execute the method.
            statusCode = client.executeMethod(httpMethod);
            stringRepresentation = httpMethod.getResponseBodyAsString();
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
        resource.setCell("representation", runtime.newText(stringRepresentation));

        return resource;
    }
}
