package userinterface;

import impresario.IModel;

//==============================================================================
public class ViewFactory {

    public static View createView(String viewName, IModel model)
    {
        if(viewName.equals("LibrarianView"))
        {
            return new LibrarianView(model);
        }
        else if(viewName.equals("TransactionChoiceView"))
        {
            return new TransactionChoiceView(model);
        }
        else if(viewName.equals("WorkerView"))
        {
            return new WorkerView(model);
        }
        else if(viewName.equals("StudentView"))
        {
            return new AddStudentBorrowerView(model);
        }
        else if(viewName.equals("BookView"))
        {
            return new BookView(model);
        }
        else if(viewName.equals("SearchWorkerView"))
        {
            return new SearchWorkersView(model);
        }
        else if(viewName.equals("SearchBooksView"))
        {
            return new SearchBooksView(model);
        }
        else if(viewName.equals("WorkerCollectionView"))
        {
            return new WorkerCollectionView(model);
        }
        else if(viewName.equals("BookCollectionView"))
        {
            return new BookCollectionView(model);
        }
        else if(viewName.equals("WithdrawTransactionView"))
        {
            return new WithdrawTransactionView(model);
        }
        else if(viewName.equals("TransferTransactionView"))
        {
            return new TransferTransactionView(model);
        }
        else if(viewName.equals("BalanceInquiryTransactionView"))
        {
            return new BalanceInquiryTransactionView(model);
        }
        else if(viewName.equals("BalanceInquiryReceipt"))
        {
            return new BalanceInquiryReceipt(model);
        }
        else if(viewName.equals("WithdrawReceipt"))
        {
            return new WithdrawReceipt(model);
        }
        else if(viewName.equals("DepositReceipt"))
        {
            return new DepositReceipt(model);
        }
        else if(viewName.equals("TransferReceipt"))
        {
            return new TransferReceipt(model);
        }
        else if(viewName.equals("ModifyWorkerView"))
        {
            return new ModifyWorkerView(model);
        }
        else if(viewName.equals("ModifyBookView"))
        {
            return new ModifyBookView(model);
        }
        else
            return null;
    }




	/*
	public static Vector createVectorView(String viewName, IModel model)
	{
		if(viewName.equals("SOME VIEW NAME") == true)
		{
			//return [A NEW VECTOR VIEW OF THAT NAME TYPE]
		}
		else
			return null;
	}
	*/

}