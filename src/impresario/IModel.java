// tabs=4
//************************************************************
//	COPYRIGHT 2003 ArchSynergy, Ltd. - ALL RIGHTS RESERVED
//
// This file is the product of ArchSynergy, Ltd. and cannot be 
// reproduced, copied, or used in any shape or form without 
// the express written consent of ArchSynergy, Ltd.
//************************************************************
//
//	$Source: /cvsroot/EZVideo/src.impresario/IModel.java,v $
//
//	Reason: The interface for a Model object in an MVC 
//			Impresario relationship.
//
//	Revision History: See end of file.
//
//*************************************************************

// JavaDoc information
/**
 * @author $Author: tomb $  @version	$Revision: 1.2 $
 */
/** @version $Revision: 1.2 $ */

// specify the package
package impresario;

// system imports

// local includes

//==============================================================
public interface IModel {
    /** A method that View objects can use for their construction */
    Object getState(String key);

    /** Allow View objects to register for state changes */
    void subscribe(String key, IView subscriber);

    void unSubscribe(String key, IView subscriber);

    /** Accept state change requests from Control objects */
    void stateChangeRequest(String key, Object value);
}

//**************************************************************
//	Revision History:
//				
//	$Log: IModel.java,v $
//	Revision 1.2  2003/10/24 05:45:19  tomb
//	Changed register to subscribe.
//	
//	Revision 1.1  2003/10/24 04:11:18  tomb
//	Revamped interfaces to more closely follow the MVC architecture.
//	
//	Revision 1.1  2003/10/24 01:11:46  tomb
//	Initial Checkin.
//	
