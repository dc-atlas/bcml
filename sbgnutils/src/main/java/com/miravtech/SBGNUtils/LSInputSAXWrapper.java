/**
 *
 * Copyright (C) 2010 Razvan Popovici <rp@miravtech.com>
 * Copyright (C) 2010 Luca Beltrame <luca.beltrame@unifi.it>
 * Copyright (C) 2010 Enrica Calura <enrica.calura@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.miravtech.SBGNUtils;


import java.io.InputStream;
import java.io.Reader;

import org.w3c.dom.ls.LSInput;
import org.xml.sax.InputSource;

/**
 * LSInput implementation that wraps a SAX InputSource
 * 
 * @author Ryan.Shoemaker@Sun.COM
 */
public class LSInputSAXWrapper implements  LSInput {
    private InputSource core;

    public LSInputSAXWrapper(InputSource inputSource) {
        assert inputSource != null;
        core = inputSource;
    }

    public Reader getCharacterStream() {
        return core.getCharacterStream();
    }

    public void setCharacterStream(Reader characterStream) {
        core.setCharacterStream(characterStream);
    }

    public InputStream getByteStream() {
        return core.getByteStream();
    }

    public void setByteStream(InputStream byteStream) {
        core.setByteStream(byteStream);
    }

    public String getStringData() {
        return null;
    }

    public void setStringData(String stringData) {
        // no-op
    }

    public String getSystemId() {
        return core.getSystemId();
    }

    public void setSystemId(String systemId) {
        core.setSystemId(systemId);
    }

    public String getPublicId() {
        return core.getPublicId();
    }

    public void setPublicId(String publicId) {
        core.setPublicId(publicId);
    }

    public String getBaseURI() {
        return null;
    }

    public void setBaseURI(String baseURI) {
        // no-op
    }

    public String getEncoding() {
        return core.getEncoding();
    }

    public void setEncoding(String encoding) {
        core.setEncoding(encoding);
    }

    public boolean getCertifiedText() {
        return true;
    }

    public void setCertifiedText(boolean certifiedText) {
        // no-op
    }
}
