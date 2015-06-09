# Introduction #

The model classes make use of the JAXB framework. Using JAXB, it's very easy to convert the object model into XML and vice versa.


# Details #

```
// lookup the binary model file
final URL url = ClassLoader.getSystemResource("models/mx16/aMERLIN.mdl");

// decode the model file into the data model
final WingedModel model = (WingedModel) HoTTDecoder.decode(new File(url.toURI()));

// create a JAXB marshaller to convert the data model into XML
final JAXBContext ctx = JAXBContext.newInstance(WingedModel.class, WingedMixer.class, WingedTrim.class);
final Marshaller m = ctx.createMarshaller();
m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

// write the XML to stdout
m.marshal(model, System.out);
```