/*July 3rd, 2014, zqian
 * based on the BN structure, to compute the statistical scores
 * input: 
 * @database@_BN (only need the BN Structure stored in Path_BayesNets)
 * output: 
 * @database@_BN (e.g. FID_BiggestRchain_Score (all the results are same as Score))
 * @database@_db (e.g. local_CT, local_CP)
 * 
 * note1:
 * For BN_ScoreComputation  UseLocal_CT should be 1, 
 * 	or the program will compute the parameters twice if you learn the structure within this program (i.e. un-comment the Structure learning part ).
    
 * note2: currently, all the local CT tables are computed directly based on our dynamic program (namely CTGenerator).
 * 		 should be ok by extracting these table from biggestRchain_CT table?
 * */

import java.sql.SQLException;

public class BN_ScoreComputation{
	static String opt1;
	public static void main( String[] args ) throws SQLException{
		
		long t1 = System.currentTimeMillis();

		long t3 = System.currentTimeMillis();


		/* Compute local CT tables and store them in @database@_db database*/
	
		if ( SubsetCTComputation.compute_subset_CT( ) != 0 ){ 
			System.out.println( "Failed to get counts." );
			return;
		}
		
		long t4 = System.currentTimeMillis();
		System.out.println( "\n************\nLocal CT time: " + ( t4 - t3 ) + "ms" );
		System.out.println( "Current runtime: " + ( t4 - t1 ) + "ms" );
		
		/* Compute Parameters (i.e. cp tables) and store them in @database@_db database
		 *@input: local_ct
		 *@output: local_cp	 */
		try	{
			local_CP.local_CP();
		}
		catch ( Exception e ){
			System.out.println( "Failed to compute conditional probabilities." );
			e.printStackTrace();
			return;
		}
		long t5 = System.currentTimeMillis();
		System.out.println( "Computing time: " + ( t5 - t4 ) + "ms" );
		System.out.println( "Current runtime: " + ( t5 - t1 ) + "ms" );
		
		/* Compute Scores to evaluate the Bayes Net, store them in @database@_BN database		 */
		if ( ScoreComputation.Compute_FID_Scores() != 0 ){
			System.out.println( "Failed to compute FID Scores." );
			return;
		}
		long t6 = System.currentTimeMillis();
		System.out.println( "Computing time: " + ( t6 - t5 ) + "ms" );
		System.out.println( "\nTotal runtime: " + ( t6 - t1 ) + "ms" );
	}
	public static void setVarsFromConfig(){
		Config conf = new Config();
		//1: run Setup; 0: not run
		opt1 = conf.getProperty("AutomaticSetup");
	}

}


