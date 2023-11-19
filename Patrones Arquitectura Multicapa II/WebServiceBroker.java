<?xml version="1.0" encoding="UTF-8"?>
<purchaseOrder orderDate="2003-01-15" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="po2.xsd">
    <shipTo>
        <name>Jo Miller</name>
        <street>Grand View Drive</street>
        <city>Bethesda</city>
        <state>MD</state>
        <zip>20816</zip>
    </shipTo>
    <billTo>
        <name>Jane Miller</name>
        <street>Grand View Drive</street>
        <city>Bethesda</city>
        <state>MD</state>
        <zip>20816</zip>
    </billTo>
    <Items>
        <id>101</id>
        <name>Core J2EE Patterns</name>
        <price>59.99</price>
    </Items>
    <Items>
        <id>102</id>
        <name>Enterprise Patterns</name>
        <price>59.99</price>
    </Items>
    <Items>
        <id>103</id>
        <name>WebService Patterns</name>
        <price>59.99</price>
    </Items>
</purchaseOrder>

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class CustomProcessorServlet extends HttpServlet {
    public String getServletInfo() {
        return "Servlet description";
    }
    protected void dispatch(HttpServletRequest request, HttpServletResponse response, String page) throws javax.servlet.ServletException, java.io.IOException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page);dispatcher.forward(request, response);
    }
    public void init() throws ServletException { }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException,java.io.IOException { }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException,java.io.IOException {
        String xmlDocument = request.getParameter("PurchaseOrder");
        JavaBinder binder = new JavaBinder();
        binder.routeDocument(xmlDocument);
    }
}

import generated.Item;
import generated.PurchaseOrder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
public class JavaBinder {
    public JavaBinder() { }
    private Object parse(String xmlDocument) {
        Object o = null;
        try {
            JAXBContext jc = JAXBContext.newInstance("generated");
            Unmarshaller u = jc.createUnmarshaller();
            o = u.unmarshal(new StreamSource( new StringReader(xmlDocument)));
        } 
        catch (JAXBException e) {
        }
        return o; 
    }
    public void routeDocument(String xmlDocument) {
        Object o = parse(xmlDocument);
        if (o instanceof PurchaseOrder) { 
            PurchaseOrder po = (PurchaseOrder) o;
            printIt(po);
            PurchaseOrderWebBroker broker = new PurchaseOrderWebBroker();
            broker.placeOrder(po);
        }
    }
    private void printIt(PurchaseOrder po) {
        System.out.println(po.getBillTo());
        System.out.println(po.getShipTo());
        System.out.println(po.getOrderDate());
        List items = po.getItems();
        Iterator i = items.iterator();
        while (i.hasNext()) {
            Item item = (Item) i.next();
            System.out.println("Id : " + item.getId() + " Name : " + item.getName() + " Price : " + item.getPrice());
        }
    } 
}

import generated.PurchaseOrder;
public class PurchaseOrderWebBroker {
    public void placeOrder(PurchaseOrder po) {
        // Do security checks here, if necessary
        OrderAppService as = new OrderAppService();
        as.placeOrder(po);
    }
}
import generated.Address;
import generated.PurchaseOrder;
import java.util.List;
public class OrderAppService {
    public OrderAppService() { }
    public void placeOrder(PurchaseOrder purchaseOrder) {
        Address shipTo = purchaseOrder.getShipTo();
        Address billTo = purchaseOrder.getBillTo();
        List items = purchaseOrder.getItems();
        // Update data store
    }
}
