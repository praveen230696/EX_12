package org.kiot.productoperation;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ProductOperation {

	Scanner scanner = new Scanner(System.in);
	public void doProductOperation() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("***********PRODUCT MANAGEMENT SYSTEM***********");
		int choice = 0;
		do {
			System.out.println("1--->Add Products to store");
			System.out.println("2--->Order Products");
			System.out.println("3--->Exit");
			System.out.println("Plese enter your choice:");
			choice = scanner.nextInt();
			switch (choice) {
				case 1: {
					if(addProducts()) {
						System.out.println("Products inserted successfully");
					} else {
						System.out.println("Products insertion failed");
					}
					break;
				} 
				case 2: {
					if(doTransaction()) {
						System.out.println("Transaction Success");
					} else {
						System.out.println("Transaction Failed");
					}
					break;
				}
				case 3: {
					System.out.println("Thank you for using our service!");
					break;
				}
				default: {
					System.out.println("Please enter correct choice");
					break;
				}
			}
		} while(3 != choice);	
	}
	
	private boolean addProducts() {
		// TODO Auto-generated method stub
		try {
			scanner.nextLine();
			System.out.println("Enter productName:");
			String productName = scanner.nextLine();
			System.out.println("Enter productPrice:");
			int productPrice = scanner.nextInt();
			System.out.println("Enter quantityOnHand:");
			int quantityOnHand = scanner.nextInt();
			Connection connection = DriverManager.getConnection
			        ("jdbc:mysql://localhost:3306/productdb",
                    "root", "root");
			String sqlQuery = "insert into product (productCode,productName, "
					+ "productPrice, quantityOnHand) values (?,?,?,?) ";
			PreparedStatement preparedStatement = connection.prepareStatement
					(sqlQuery);
			preparedStatement.setInt(1, 0);
			preparedStatement.setString(2, productName);
			preparedStatement.setInt(3, productPrice);
			preparedStatement.setInt(4, quantityOnHand);
			int row = preparedStatement.executeUpdate();
			if (0 != row) {
				return true;
			}
			} catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			return false;
	}

	private boolean doTransaction() throws Exception {
		System.out.println("How many products you would like to order:");
		int numberOfProducts = scanner.nextInt();
		int transaction = 1;
		int productsEntry = 1;
		String transactionStatus = "failed";

		XSSFWorkbook transactionWorkbook = new XSSFWorkbook();
		XSSFSheet transactionSpreadSheet = transactionWorkbook.createSheet
				("transaction db");
		XSSFRow transactionRow = transactionSpreadSheet.createRow(0);
		XSSFCell tranactionCell;
				
		tranactionCell = transactionRow.createCell(0);
		tranactionCell.setCellValue("TransactionNo");

		tranactionCell = transactionRow.createCell(1);
		tranactionCell.setCellValue("ProductCode");

		tranactionCell = transactionRow.createCell(2);
		tranactionCell.setCellValue("QuantityOfOrder");
			
		XSSFWorkbook shippingWorkBook = new XSSFWorkbook();

			// Create a spreadsheet inside a workbook
	    XSSFSheet shippingSpreadSheet
			= shippingWorkBook.createSheet("shipping db");

	    XSSFWorkbook pendingWorkBook = new XSSFWorkbook();

			// Create a spreadsheet inside a workbook
	    XSSFSheet pendingSpreadSheet
			= pendingWorkBook.createSheet("pending db");
	    	
		while(productsEntry <= numberOfProducts) {
			System.out.println("TRANSACTION NO " + productsEntry);
			System.out.println("Enter your product code:");
			int productCode = scanner.nextInt();
		
			System.out.println("Enter product quantity:");
			int productQuantity = scanner.nextInt();

			transactionRow = transactionSpreadSheet.createRow(productsEntry);
			tranactionCell = transactionRow.createCell(0);
			tranactionCell.setCellValue(transaction);
	
			tranactionCell = transactionRow.createCell(1);
			tranactionCell.setCellValue(productCode);
		
			tranactionCell = transactionRow.createCell(2);
			tranactionCell.setCellValue(productQuantity);
				
			Connection connection = DriverManager.getConnection
                    ("jdbc:mysql://localhost:3306/productdb","root", "root");
			String quantityOnHandSQLQuery = "select quantityOnHand from"
					+ " product where productCode = ? ";
			PreparedStatement quantityOnHandPreparedStatement = 
					connection.prepareStatement(quantityOnHandSQLQuery);
			quantityOnHandPreparedStatement.setInt(1,productCode);
			ResultSet quantityOnHandResultSet = 
					quantityOnHandPreparedStatement.executeQuery();
			int quantityOnHand = 0;
		    while (quantityOnHandResultSet.next()) {
		    	quantityOnHand = quantityOnHandResultSet.getInt
		    			("quantityOnHand");
		    }
		    
		    
		    String productCodeSQLQuery = "select productCode from product"
		    		+ " where productCode = ? ";
			PreparedStatement productCodePreparedStatement = 
					connection.prepareStatement(productCodeSQLQuery);
			productCodePreparedStatement.setInt(1,productCode);
			ResultSet productCodeResultSet = 
					productCodePreparedStatement.executeQuery();
			boolean productCodeFound = false;
			if(productCodeResultSet.next()) {
				if(productCode == productCodeResultSet.getInt("productCode")) {
					productCodeFound = true;
					//System.out.println("productCodeFound");
				} 
			}
			if(productCodeFound && productQuantity <= quantityOnHand) {
				XSSFRow shippingRow = shippingSpreadSheet.createRow(0);
				XSSFCell shippingCell;
					
				shippingCell = shippingRow.createCell(0);
				shippingCell.setCellValue("ShippingTransactionNo");

				shippingCell = shippingRow.createCell(1);
				shippingCell.setCellValue("Price");

				shippingRow = shippingSpreadSheet.createRow(productsEntry);
				shippingCell = shippingRow.createCell(0);
				shippingCell.setCellValue(transaction);
			
				shippingCell = shippingRow.createCell(1);
				shippingCell.setCellValue(productCode);
		
				String productPriceSQLQuery = "select productPrice from product"
						+ " where productCode = ? ";
				PreparedStatement productPricePreparedStatement = 
						connection.prepareStatement(productPriceSQLQuery);
				productPricePreparedStatement.setInt(1,productCode);
				ResultSet productPriceResultSet = 
						productPricePreparedStatement.executeQuery();
				int price = 0;
				int updatedQuantityHand = 0;
				while (productPriceResultSet.next()) {
					price = productPriceResultSet.getInt("productPrice");
					XSSFRow pendingRow = 
							shippingSpreadSheet.createRow(0);
					XSSFCell pendingCell;
						
					pendingCell = pendingRow.createCell(0);
					pendingCell.setCellValue("ShippingTransactionNo");

					pendingCell = pendingRow.createCell(1);
					pendingCell.setCellValue("Price");

					pendingRow = shippingSpreadSheet.createRow(productsEntry);
					pendingCell = pendingRow.createCell(0);
					pendingCell.setCellValue(transaction);
				
					pendingCell = pendingRow.createCell(1);
					pendingCell.setCellValue(productQuantity * price);
				}
					updatedQuantityHand = quantityOnHand -  productQuantity;
					String updatedProductPrice = "update product set "
							+ "quantityOnHand = ? where productCode = ?";
					PreparedStatement updatedProductPricePreparedStatement = 
							connection.prepareStatement(updatedProductPrice);
					updatedProductPricePreparedStatement.setInt
							(1,updatedQuantityHand);
					updatedProductPricePreparedStatement.setInt(2,productCode);
					int row = updatedProductPricePreparedStatement
							.executeUpdate();
					if(row == 1) {
						transactionStatus = "Success";
					} else {
						transactionStatus = "Failed";
					}
					
			}  else if(!productCodeFound){
				System.out.println("Sorry Product code not available in the "
						+ "store");
				XSSFRow pendingRow = pendingSpreadSheet.createRow(0);
				XSSFCell pendingCell;
				pendingCell = pendingRow.createCell(0);
				pendingCell.setCellValue("PendingTransactionNo");
				pendingCell = pendingRow.createCell(1);
				pendingCell.setCellValue("Status");
					
				pendingRow = pendingSpreadSheet.createRow(productsEntry);
				pendingCell = pendingRow.createCell(0);
				pendingCell.setCellValue(transaction);
	
				pendingCell = pendingRow.createCell(1);
				pendingCell.setCellValue("NA");
			} else if (productQuantity > quantityOnHand){
				System.out.println("Sorry Product requested is gretaer "
						+ "than quantity on hand");
				XSSFRow pendingRow = pendingSpreadSheet.createRow(0);
				XSSFCell pendingCell;
				pendingCell = pendingRow.createCell(0);
				pendingCell.setCellValue("PendingTransactionNo");
					
				pendingCell = pendingRow.createCell(1);
				pendingCell.setCellValue("Status");

				pendingRow = pendingSpreadSheet.createRow(productsEntry);
				pendingCell = pendingRow.createCell(0);
				pendingCell.setCellValue(transaction);
		
				pendingCell = pendingRow.createCell(1);
				pendingCell.setCellValue("NS");
			}		    			
			productsEntry++;
			transaction++;
		}
		
		
		FileOutputStream pendingStatusFile = new FileOutputStream(new File
				("PendingStatus.xlsx"));
		//write
		if (pendingStatusFile != null) {
			pendingWorkBook.write(pendingStatusFile);
			System.out.println("Pendingdatabase.xlsx written "
					+ "successfully");
		} 
		
		FileOutputStream shippingFile = new FileOutputStream
				(new File("Shipping.xlsx"));
		// write
		if(shippingFile != null) {
			shippingWorkBook.write(shippingFile);
			if(transactionStatus.equalsIgnoreCase("Success")) {
				System.out.println("Shippingdatabase.xlsx written "
						+ "successfully");
				shippingFile.close();
			}
			//return true;
		} 
	
		// Local directory on computer
		FileOutputStream transactionFile = new FileOutputStream
				(new File("Transaction.xlsx"));
		// 	write
		if(transactionFile != null) {
			transactionWorkbook.write(transactionFile);
			System.out.println("Transactiondatabase.xlsx written "
					+ "successfully");
			transactionFile.close();
			if(transactionStatus.equalsIgnoreCase("Success"))
				return true;
		}  
		return false;			
	}
}
