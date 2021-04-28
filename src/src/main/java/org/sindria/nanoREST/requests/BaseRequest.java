package org.sindria.nanoREST.requests;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;

import java.util.*;

/**
 * BaseRequest represents an HTTP request.
 *
 * @author Luca Pitzoi <luca.pitzoi@sindria.org>
 */
public abstract class BaseRequest {

    private static final String HEADER_FORWARDED = "0b00001"; // When using RFC 7239
    private static final String HEADER_X_FORWARDED_FOR = "0b00010";
    private static final String HEADER_X_FORWARDED_HOST = "0b00100";
    private static final String HEADER_X_FORWARDED_PROTO = "0b01000";
    private static final String HEADER_X_FORWARDED_PORT = "0b10000";
    private static final String HEADER_X_FORWARDED_ALL = "0b11110"; // All "X-Forwarded-*" headers
    private static final String HEADER_X_FORWARDED_AWS_ELB = "0b11010"; // AWS ELB doesn't send X-Forwarded-Host

    private static String[] trustedProxies;


    private static final String METHOD_HEAD = "HEAD";
    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";
    private static final String METHOD_PUT = "PUT";
    private static final String METHOD_PATCH = "PATCH";
    private static final String METHOD_DELETE = "DELETE";
    private static final String METHOD_PURGE = "PURGE";
    private static final String METHOD_OPTIONS = "OPTIONS";
    private static final String METHOD_TRACE = "TRACE";
    private static final String METHOD_CONNECT = "CONNECT";


    private final RouterNanoHTTPD.UriResource uriResourceHTTPD;

    private final Map<String, String> urlParamsHTTPD;

    private final NanoHTTPD.IHTTPSession sessionHTTPD;

    public String request;

    public Collection<String> query;

    public String files;

    public NanoHTTPD.CookieHandler cookies;

    public Map<String, String> headers;

    protected String content;

    protected String languages;

    protected String charsets;

    protected String encodings;

    public String requestUri;

    protected NanoHTTPD.Method method;

    protected String session;

    protected String locale;

    protected String defaultLocale = "en";


    private final Boolean isHostValid = true;
    private final Boolean isForwardedValid = true;

    private static Integer trustedHeaderSet = -1;


    private static final Map<String, String> forwardedParams = new HashMap<>();
    static {
        forwardedParams.put(BaseRequest.HEADER_X_FORWARDED_FOR, "for");
        forwardedParams.put(BaseRequest.HEADER_X_FORWARDED_HOST, "host");
        forwardedParams.put(BaseRequest.HEADER_X_FORWARDED_PROTO, "proto");
        forwardedParams.put(BaseRequest.HEADER_X_FORWARDED_PORT, "host");
    };

    /**
     * Names for headers that can be trusted when
     * using trusted proxies.
     *
     * The FORWARDED header is the standard as of rfc7239.
     *
     * The other headers are non-standard, but widely used
     * by popular reverse proxies (like Apache mod_proxy or Amazon EC2).
     */
     private static final Map<String, String> trustedHeaders = new HashMap<>();
     static {
         trustedHeaders.put(BaseRequest.HEADER_FORWARDED, "FORWARDED");
         trustedHeaders.put(BaseRequest.HEADER_X_FORWARDED_FOR, "X_FORWARDED_FOR");
         trustedHeaders.put(BaseRequest.HEADER_X_FORWARDED_HOST, "X_FORWARDED_HOST");
         trustedHeaders.put(BaseRequest.HEADER_X_FORWARDED_PROTO, "X_FORWARDED_PROTO");
         trustedHeaders.put(BaseRequest.HEADER_X_FORWARDED_PORT, "X_FORWARDED_PORT");
     };

    /**
     * BaseRequest constructor
     */
    public BaseRequest(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
        this.uriResourceHTTPD = uriResource;
        this.urlParamsHTTPD = urlParams;
        this.sessionHTTPD = session;
        this.inizialize();
    }

    /**
     * Sets the parameters for this request.
     *
     * This method also re-initializes all properties.
     */
    public void inizialize() {
        this.query = this.urlParamsHTTPD.values();
        this.cookies = this.sessionHTTPD.getCookies();
        this.headers = this.sessionHTTPD.getHeaders();
        this.requestUri = this.uriResourceHTTPD.getUri();
        this.method = this.sessionHTTPD.getMethod();
    }


//    /**
//     * Initializes HTTP request formats.
//     */
//    protected static function initializeFormats()
//    {
//        static::$formats = [
//        'html' => ['text/html', 'application/xhtml+xml'],
//        'txt' => ['text/plain'],
//        'js' => ['application/javascript', 'application/x-javascript', 'text/javascript'],
//        'css' => ['text/css'],
//        'json' => ['application/json', 'application/x-json'],
//        'jsonld' => ['application/ld+json'],
//        'xml' => ['text/xml', 'application/xml', 'application/x-xml'],
//        'rdf' => ['application/rdf+xml'],
//        'atom' => ['application/atom+xml'],
//        'rss' => ['application/rss+xml'],
//        'form' => ['application/x-www-form-urlencoded'],
//        ];
//    }


    public NanoHTTPD.Method getMethod() {
        return this.method;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public NanoHTTPD.CookieHandler getCookies() {
        return this.cookies;
    }

    /**
     * Sets a list of trusted proxies.
     *
     * You should only list the reverse proxies that you manage directly.
     */
    public static void setTrustedProxies(String[] proxies, Integer trustedHeaderSet) {

        int i = 0;
        for (String proxy: proxies) {
            BaseRequest.trustedProxies[i] = proxy;
            i++;
        }

        BaseRequest.trustedHeaderSet = trustedHeaderSet;
    }

    /**
     * Indicates whether this request originated from a trusted proxy.
     * true if the request came from a trusted proxy, false otherwise
     */
    public Boolean isFromTrustedProxy() {
        return false;
        // TODO: implement
    }

    /**
     * Checks whether the request is secure or not.
     *
     * This method can read the client protocol from the "X-Forwarded-Proto" header
     * when trusted proxies were set via "setTrustedProxies()".
     *
     * The "X-Forwarded-Proto" header must contain the protocol: "https" or "http".
     */
    public boolean isSecure() {
        return false;
        // TODO: implement



    }

    /**
     * Gets the list of trusted proxies.
     */
    public static String[] getTrustedProxies() {
        return BaseRequest.trustedProxies;
    }

    /**
     * Gets the set of trusted headers from trusted proxies.
     */
    public static Integer getTrustedHeaderSet() {
        return BaseRequest.trustedHeaderSet;
    }


    /**
     * Normalizes a query string.
     *
     * It builds a normalized query string, where keys/value pairs are alphabetized,
     * have consistent escaping and unneeded delimiters are removed.
     */
    public static String normalizeQueryString() {
        return "";
        // TODO: implement
    }

    /**
     * Returns true if the request is a XMLHttpRequest.
     *
     * It works if your JavaScript library sets an X-Requested-With HTTP header.
     * It is known to work with common JavaScript frameworks:
     *
     * @see "https://wikipedia.org/wiki/List_of_Ajax_frameworks#JavaScript"
     */
    public Boolean isXmlHttpRequest() {
        return false;
        // TODO: implement
    }

    /**
     * Gets the mime type associated with the format.
     */
    public String getMimeType() {
        return "";
        // TODO: implement
    }

    /**
     * Gets the format associated with the request.
     */
    public String getContentType() {
       return "";
        // TODO: implement
    }


    /**
     * Checks if the request method is of specified type.
     */
    public Boolean isMethod(String method) {
        return true;
        // TODO: implement
    }

    /**
     * Checks whether the method is cacheable or not.
     * True for GET and HEAD, false otherwise
     *
     * @see "https://tools.ietf.org/html/rfc7231#section-4.2.3"
     */
    public Boolean isMethodCacheable() {
        return false;
        // TODO: implement
    }

    /**
     * Returns the protocol version.
     */
    public String getProtocolVersion() {
        return "";
        // TODO: implement
    }


    /**
     * Returns the request body content.
     */
    public String getContent() {
        return "";
        // TODO: implement
    }

    /**
     * Gets a list of languages acceptable by the client browser.
     */
    public String getLanguages() {
        if (this.languages != null) {
            return this.languages;
        }
        // TODO: implement
        return null;
    }

    /**
     * Get the locale.
     */
    public String getLocale() {
        if (this.locale != null) {
            return this.locale;
        }
        // TODO: implement
        return null;
    }

    /**
     * Get the default locale.
     */
    public String getDefaultLocale() {
        return this.defaultLocale;
    }

    /**
     * Gets a list of charsets acceptable by the client browser.
     */
    public String getCharsets() {
        if (this.charsets != null) {
            return this.charsets;
        }
        // TODO: implement
        return null;
    }

    /**
     * Gets a list of encodings acceptable by the client browser.
     */
    public String getEncodings() {
        if (this.encodings != null) {
            return this.encodings;
        }
        // TODO: implement
        return null;
    }
}
