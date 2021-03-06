//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.18 at 10:22:27 PM CST 
//


package com.openfms.utils.dcp.smpte.pkl;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.openfms.utils.dcp.smpte.pkl package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AssetMap_QNAME = new QName("http://www.smpte-ra.org/schemas/429-9/2007/AM", "AssetMap");
    private final static QName _VolumeIndex_QNAME = new QName("http://www.smpte-ra.org/schemas/429-9/2007/AM", "VolumeIndex");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.openfms.utils.dcp.smpte.pkl
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AssetType }
     * 
     */
    public AssetType createAssetType() {
        return new AssetType();
    }

    /**
     * Create an instance of {@link AssetMapType }
     * 
     */
    public AssetMapType createAssetMapType() {
        return new AssetMapType();
    }

    /**
     * Create an instance of {@link VolumeIndexType }
     * 
     */
    public VolumeIndexType createVolumeIndexType() {
        return new VolumeIndexType();
    }

    /**
     * Create an instance of {@link ChunkType }
     * 
     */
    public ChunkType createChunkType() {
        return new ChunkType();
    }

    /**
     * Create an instance of {@link UserText }
     * 
     */
    public UserText createUserText() {
        return new UserText();
    }

    /**
     * Create an instance of {@link AssetType.ChunkList }
     * 
     */
    public AssetType.ChunkList createAssetTypeChunkList() {
        return new AssetType.ChunkList();
    }

    /**
     * Create an instance of {@link AssetMapType.AssetList }
     * 
     */
    public AssetMapType.AssetList createAssetMapTypeAssetList() {
        return new AssetMapType.AssetList();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AssetMapType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.smpte-ra.org/schemas/429-9/2007/AM", name = "AssetMap")
    public JAXBElement<AssetMapType> createAssetMap(AssetMapType value) {
        return new JAXBElement<AssetMapType>(_AssetMap_QNAME, AssetMapType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VolumeIndexType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.smpte-ra.org/schemas/429-9/2007/AM", name = "VolumeIndex")
    public JAXBElement<VolumeIndexType> createVolumeIndex(VolumeIndexType value) {
        return new JAXBElement<VolumeIndexType>(_VolumeIndex_QNAME, VolumeIndexType.class, null, value);
    }

}
