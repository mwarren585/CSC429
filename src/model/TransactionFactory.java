// specify the package
package model;

// system imports

// project imports

/** The class containing the TransactionFactory for the ATM application */
//==============================================================
public class TransactionFactory
{

    /**
     *
     */
    //----------------------------------------------------------
    public static Transaction createTransaction(String transType,
                                                Worker cust)
            throws Exception
    {
        Transaction retValue = null;

		/*if (transType.equals("Deposit") == true)
		{
			retValue = new DepositTransaction(cust);
		}
		else
		if (transType.equals("Withdraw") == true)
		{
			retValue = new WithdrawTransaction(cust);
		}
		else
		if (transType.equals("Transfer") == true)
		{
			retValue = new TransferTransaction(cust);
		}
		else
		if (transType.equals("BalanceInquiry") == true)
		{
			retValue = new BalanceInquiryTransaction(cust);
		}
		else
		if (transType.equals("ImposeServiceCharge") == true)
		{
			retValue = new ImposeServiceChargeTransaction(cust);
		}
          */
        return retValue;
    }
}