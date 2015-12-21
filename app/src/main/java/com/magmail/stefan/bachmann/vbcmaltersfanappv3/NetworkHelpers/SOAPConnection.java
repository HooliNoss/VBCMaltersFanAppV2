package com.magmail.stefan.bachmann.vbcmaltersfanappv3.NetworkHelpers;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class SOAPConnection
{
    private String _NAMESPACE = "http://myvolley.swissvolley.ch";
    //private String _NAMESPACE = "http://schemas.xmlsoap.org/soap/envelope/";

    //private String _URL = "http://80.74.154.73/svserver.php";
    private String _URL = "http://myvolley.volleyball.ch/svserver.php";

    SoapObject mResult;
    String mSoapAction;
    String mMethodName;
    String mParameterName;
    String mParameterValue;

    public SOAPConnection()
    {

    }

    public SoapObject getSOAPConnection(String soapAction, String methodName, String parameterName, String parameterValue)
    {
        mSoapAction = soapAction;
        mMethodName = methodName;
        mParameterName = parameterName;
        mParameterValue = parameterValue;

        try
        {
            SoapObject request = new SoapObject(_NAMESPACE, mMethodName);

            //Initialize soap request + add parameters

            //Use this to add parameters
            request.addProperty(mParameterName, mParameterValue);

            //Declare the version of the SOAP request
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = false;

            //Needed to make the internet call
            HttpTransportSE androidHttpTransport = new HttpTransportSE(_URL);
            //androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            try
            {
                //this is the actual part that will call the webservice
                androidHttpTransport.call(mSoapAction, envelope);
            } catch (Exception e)
            {
                e.printStackTrace();
            }

            // Get the SoapResult from the envelope body.
            mResult = (SoapObject)envelope.bodyIn;

            return mResult;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
}