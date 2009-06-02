/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package com.ragnarson.ioke.couchdb;

import ioke.lang.IokeObject;
import ioke.lang.Runtime;
import ioke.lang.exceptions.ControlFlow;
import ioke.lang.extensions.Extension;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;

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
        
        resource.setCell("mimeType", runtime.newText("application/json"));
        resource.setCell("representation", runtime.nil);
        resource.setCell("result", runtime.newNumber(0));

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

    private class Get extends ResourceMethod {
        public static final String DOC = "executes GET request on resource";

        public Get() {
            super("get");
        }

		@Override
		public HttpMethod getHttpMethod(String url) {
			return new GetMethod(url);
		}
    }

    private class Put extends ResourceMethod {
        public static final String DOC = "executes PUT request on resource";

        public Put() {
            super("put");
        }

		@Override
		public HttpMethod getHttpMethod(String url) {
			return new PutMethod(url);
		}
    }

    private class Delete extends ResourceMethod {
        public static final String DOC = "executes DELETE request on resource";

        public Delete() {
            super("delete");
        }

		@Override
		public HttpMethod getHttpMethod(String url) {
			return new DeleteMethod(url);
		}
    }

    private class Post extends ResourceMethod {
        public static final String DOC = "executes POST request on resource";

        public Post() {
            super("post");
        }

		@Override
		public HttpMethod getHttpMethod(String url) {
			return new PostMethod(url);
		}
    }

}
