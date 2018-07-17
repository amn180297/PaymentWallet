package com.cg.paymentwalletsystem.dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.cg.paymentwalletsystem.bean.Customer;

public class PaymentWalletDaoImpl implements IPaymentDao {

	private static Map<String, Customer> myMap = null;
	private static Map<String, StringBuilder> printingList = null;
	static {
		myMap = new HashMap<String, Customer>();
		printingList = new HashMap<String, StringBuilder>();
	}

	@Override
	public Customer getCustomerDetails(String mobileNumber) {
		return myMap.get(mobileNumber);
	}

	@Override
	public void createAccount(Customer customer) {
		myMap.put(customer.getMobileNumber(), customer);
		StringBuilder builder = new StringBuilder("Account Created on \t" + LocalDateTime.now()
				+ "\n--------------------------------------------------------------------------\n"
				+ String.format("%-10s  %-20s%-30s %-20s", "Amount ", "Transaction Type", "Date & Time", "Balance")
				+ "\n--------------------------------------------------------------------------\n");
		printingList.put(customer.getMobileNumber(), builder);
	}

	@Override
	public void depositMoney(Customer customer, BigDecimal depositAmount) {
		if (myMap.get(customer.getMobileNumber()) != null) {
			customer.setWalletBalance(customer.getWalletBalance().add(depositAmount));
			StringBuilder builder = new StringBuilder();
			builder.append(String.format("\n" + "%-10s  %-20s%-30s %-1s", "\u20B9 " + depositAmount, "Deposited",
					LocalDateTime.now(), "\u20B9 " + customer.getWalletBalance()));
			printingList.put(customer.getMobileNumber(), printingList.get(customer.getMobileNumber()).append(builder));
		}
	}

	@Override
	public boolean withdrawMoney(Customer customer, BigDecimal withdrawAmount) {
		boolean status = false;
		if (myMap.get(customer.getMobileNumber()) != null) {
			int res = customer.getWalletBalance().subtract(withdrawAmount).compareTo(new BigDecimal("1000"));
			if (res == 1) {
				customer.setWalletBalance(customer.getWalletBalance().subtract(withdrawAmount));
				StringBuilder builder = new StringBuilder();
				builder.append(String.format("\n" + "%-10s  %-20s%-30s %-1s", "\u20B9 " + withdrawAmount, "Withdrawn",
						LocalDateTime.now(), "\u20B9 " + customer.getWalletBalance()));
				printingList.put(customer.getMobileNumber(),
						printingList.get(customer.getMobileNumber()).append(builder));
				status = true;
			}
		}
		return status;
	}

	@Override
	public boolean fundTransfer(Customer sendCustomer, Customer recCustomer, BigDecimal transferAmount) {
		boolean status = false;
		if (myMap.get(sendCustomer.getMobileNumber()) != null) {
			if (myMap.get(recCustomer.getMobileNumber()) != null) {
				int res = sendCustomer.getWalletBalance().subtract(transferAmount).compareTo(new BigDecimal("1000"));
				if (res == 1) {
					sendCustomer.setWalletBalance(sendCustomer.getWalletBalance().subtract(transferAmount));
					recCustomer.setWalletBalance(recCustomer.getWalletBalance().add(transferAmount));
					StringBuilder builder = new StringBuilder();
					builder.append(String.format("\n" + "%-10s  %-20s%-30s %-1s", "\u20B9  " + transferAmount,
							"Transfered To", LocalDateTime.now(), "\u20B9 " + sendCustomer.getWalletBalance()));
					printingList.put(sendCustomer.getMobileNumber(),
							printingList.get(sendCustomer.getMobileNumber()).append(builder));

					// receiver printingList
					StringBuilder recBuilder = new StringBuilder();
					recBuilder.append(String.format("\n" + "%-10s  %-20s%-30s %-1s", "\u20B9  " + transferAmount,
							"Transfered From", LocalDateTime.now(), "\u20B9 " + recCustomer.getWalletBalance()));
					printingList.put(recCustomer.getMobileNumber(),
							printingList.get(recCustomer.getMobileNumber()).append(recBuilder));
					status = true;
				}
			}
		}
		return status;
	}

	@Override
	public StringBuilder printTransaction(String mobileNumber) {
		return printingList.get(mobileNumber);
	}

}
