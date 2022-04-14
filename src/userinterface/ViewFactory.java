package userinterface;

import impresario.IModel;

//==============================================================================
public class ViewFactory {

    public static View createView(String viewName, IModel model)
    {
        switch (viewName) {
            case "LibrarianView":
                return new LibrarianView(model);
            case "TransactionChoiceView":
                return new TransactionChoiceView(model);
            case "WorkerView":
                return new WorkerView(model);
            case "StudentView":
                return new AddStudentBorrowerView(model);
            case "BookView":
                return new BookView(model);
            case "SearchWorkerView":
                return new SearchWorkersView(model);
            case "SearchBooksView":
                return new SearchBooksView(model);
            case "WorkerCollectionView":
                return new WorkerCollectionView(model);
            case "BookCollectionView":
                return new BookCollectionView(model);
            case "WithdrawTransactionView":
                return new WithdrawTransactionView(model);
            case "TransferTransactionView":
                return new TransferTransactionView(model);
            case "BalanceInquiryTransactionView":
                return new BalanceInquiryTransactionView(model);
            case "BalanceInquiryReceipt":
                return new BalanceInquiryReceipt(model);
            case "WithdrawReceipt":
                return new WithdrawReceipt(model);
            case "DepositReceipt":
                return new DepositReceipt(model);
            case "TransferReceipt":
                return new TransferReceipt(model);
            case "ModifyWorkerView":
                return new ModifyWorkerView(model);
            case "ModifyBookView":
                return new ModifyBookView(model);
            case "CheckInBookView":
                return new CheckInBookView(model);
            default:
                return null;
        }
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