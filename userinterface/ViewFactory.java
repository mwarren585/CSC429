package userinterface;

import impresario.IModel;

//==============================================================================
public class ViewFactory {

	public static View createView(String viewName, IModel model)
	{
		if(viewName.equals("LibrarianView") == true)
		{
			return new LibrarianView(model);
		}
		else if(viewName.equals("TransactionChoiceView") == true)
		{
			return new TransactionChoiceView(model);
		}
		else if(viewName.equals("WorkerView") == true)
		{
			return new WorkerView(model);
		}
		else if(viewName.equals("StudentView") == true)
		{
			return new AddStudentBorrowerView(model);
		}
		else if(viewName.equals("BookView") == true)
		{
			return new BookView(model);
		}
		else if(viewName.equals("SearchWorkerView") == true)
		{
			return new SearchWorkersView(model);
		}
		else if(viewName.equals("WorkerCollectionView") == true)
		{
			return new WorkerCollectionView(model);
		}

		else if(viewName.equals("ModifyWorkerView") == true)
		{
			return new ModifyWorkerView(model);
		}
		else if(viewName.equals("DeleteWorkerView") == true)
		{
			return new DeleteWorkerView(model);
		}
		else if(viewName.equals("SearchBookView") == true)
		{
			return new SearchBooksView(model);
		}
		else if(viewName.equals("ModifyBookView") == true)
		{
			return new ModifyBookView(model);
		}
		else if(viewName.equals("DeleteBookView") == true)
		{
			return new DeleteBookView(model);
		}
		else if(viewName.equals("BookCollectionView") == true)
		{
			return new BookCollectionView(model);
		}
		else if(viewName.equals("TransferReceipt") == true)
		{
			return new TransferReceipt(model);
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
