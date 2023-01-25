package com.nellshark.springbootblog.config;

import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class CharResponseWrapperConfig extends HttpServletResponseWrapper {
    private final CharArrayWriter charWriter;
    private PrintWriter writer;
    private boolean getOutputStreamCalled;
    private boolean getWriterCalled;

    public CharResponseWrapperConfig(HttpServletResponse response) {
        super(response);

        charWriter = new CharArrayWriter();
    }

    public ServletOutputStream getOutputStream() throws IOException {
        if (getWriterCalled) throw new IllegalStateException("getWriter already called");

        getOutputStreamCalled = true;
        return super.getOutputStream();
    }

    public PrintWriter getWriter() {
        if (writer != null) return writer;
        if (getOutputStreamCalled) throw new IllegalStateException("getOutputStream already called");

        getWriterCalled = true;
        writer = new PrintWriter(charWriter);
        return writer;
    }

    public String toString() {
        return (writer != null) ? charWriter.toString() : null;
    }
}