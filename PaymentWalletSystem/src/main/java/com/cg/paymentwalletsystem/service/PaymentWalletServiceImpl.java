package com.cg.paymentwalletsystem.service;

import java.math.BigDecimal;

import com.cg.paymentwalletsystem.bean.Customer;
import com.cg.paymentwalletsystem.dao.IPaymentDao;
import com.cg.paymentwalletsystem.dao.PaymentWalletDaoImpl;
import com.cg.paymentwalletsystem.exception.IPaymentWalletException;
import com.cg.paymentwalletsystem.exception.PaymentWalletException;

public class PaymentWalletServiceImpl implements IPaymentService {

	IPaymentDao dao = null;

	public PaymentWalletServiceImpl() {
		dao = new PaymentWalletDaoImpl();
	}

	@Override
	public boolean validateDetails(String mobileNumber, String name, String email) throws PaymentWalletException {
		boolean result = true;
		if (!(mobileNumber.trim().length() == 10 && mobileNumber.trim().matches("^[0-9]+$"))) {
			throw new PaymentWalletException(IPaymentWalletException.MESSAGE1);
		}
		String tempName = name.trim();

		if (!(tempName.replaceAll("\\s+", "").matches("^[A-Za-z]*$"))) {
			throw new PaymentWalletException(IPaymentWalletException.MESSAGE2);
		}
		if (!(email.trim().matches("^[A-Za-z]{1}[A-Za-z0-9_.]{1,}[@]{1}[A-Za-z0-9]{1,}[.]{1}[A-Za-z]{2,3}$"))) {
			throw new PaymentWalletException(IPaymentWalletException.MESSAGE3);
		}
		return result;
	}

	@Override
	public void createAccount(Customer customer) {
		dao.createAccount(customer);
	}

	@Override
	public boolean validateMobileNumber(String mobileNumber) throws PaymentWalletException {
		boolean result = true;
		if (!(mobileNumber.trim().length() == 10 && mobileNumber.trim().matches("^[0-9]+$"))) {
			throw new PaymentWalletException(IPaymentWalletException.MESSAGE1);
		}
		return result;
	}

	@Override
	public Customer getCustomerDetails(String mobileNumber) {
		return dao.getCustomerDetails(mobileNumber);
	}

	@Override
	public boolean validateAmount(String amount) throws PaymentWalletException {
		boolean result = true;
		if (!(amount.trim().matches("^[0-9.]+$"))) {
			throw new PaymentWalletException(IPaymentWalletException.MESSAGE4);
		}
		return result;
	}

	@Override
	public void depositMoney(Customer customer, BigDecimal depositAmount) {
		dao.depositMoney(customer, depositAmount);

	}

	@Override
	public boolean withdrawMoney(Customer customer, BigDecimal depositAmount) {
		return dao.withdrawMoney(customer, depositAmount);
	}

	@Override
	public boolean fundTransfer(Customer sendCustomer, Customer recCustomer, BigDecimal depositAmount) {
		return dao.fundTransfer(sendCustomer, recCustomer, depositAmount);
	}

	@Override
	public StringBuilder printTransaction(String mobileNumber) {
		return dao.printTransaction(mobileNumber);
	}

}
