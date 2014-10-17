package com.WhiteBoard.Algorithm;

import java.util.ArrayList;

import android.content.Context;
import android.view.WindowManager;

import com.WhiteBoard.BaseClasses.RoughMessage;
import com.WhiteBoard.Send.CommunityFactory;
import com.WhiteBoard.Test.LSTDG.SendMessageInfo;
import com.baidu.mapapi.map.MapView;

public class MessageDivision {

	private static double lab;
	private static double lob;
	
	public static double getLab(){
		return lab;
	}
	
	public static double getLob(){
		return lob;
	}
	
	// Get Group
	public static ArrayList<ArrayList<RoughMessage>> groupDisplay(
			Context context, MapView mapview, String uid, String latitude,
			String longitude, String category, String[] method) {

		// get mapview arguments
		double cLongitude = (mapview.getMapCenter().getLongitudeE6() / 1E6);
		double cLatitude = (mapview.getMapCenter().getLatitudeE6() / 1E6);
		double rangeLatitude = (mapview.getLatitudeSpan() / 1E6) * 10;
		double rangeLongitude = (mapview.getLongitudeSpan() / 1E6) * 10;
		double dLatitude = cLatitude - (rangeLatitude / 2);
		double lLongitude = cLongitude - (rangeLongitude / 2);
		
		ArrayList<RoughMessage> rmList = new ArrayList<RoughMessage>();
		
		System.out.println("latitude is " + latitude );
		System.out.println("longitude is " + longitude );
		System.out.println("rangeLatitude is " + rangeLatitude / 2 );
		System.out.println("rangeLongitude is " + rangeLongitude / 2 );
		
		if (method[0].equals("Refresh")) {
			rmList = CommunityFactory.getPinnedList(uid, latitude, longitude, "" + rangeLatitude / 2, "" + rangeLongitude / 2, category);
		}
		
		System.out.println("rmList's size is" + rmList.size());
		
		// get window arguments
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		@SuppressWarnings("deprecation")
		int pxWidth = wm.getDefaultDisplay().getWidth();
		@SuppressWarnings("deprecation")
		int pxHeight = wm.getDefaultDisplay().getHeight();
		int dpWidth = (int) (pxWidth / 1.5 + 0.5);
		int dpHeight = (int) (pxHeight / 1.5 + 0.5) - 112;
		int widthBlock = (int) (dpWidth / 48);
		int heightBlock = (int) (dpHeight / 48);
		
		
		
		// get a block's range
		lab = (rangeLatitude / heightBlock);
		lob = (rangeLongitude / widthBlock);
		
//		System.out.println("lob is " + lob);
//		System.out.println("lab is " + lab);
		
		// division
		ArrayList<ArrayList<RoughMessage>> blockMessages = new ArrayList<ArrayList<RoughMessage>>();
		for (int i = 0; i < widthBlock; i++) {
			for (int j = 0; j < heightBlock; j++) {
				System.out.println("latitude is from " + (dLatitude + lab * j) + " to " + (dLatitude + lab * (j + 1)));
				System.out.println("longitude is from " + (lLongitude + lob * i) + " to " + ( lLongitude + lob * (i + 1)));
				
				ArrayList<RoughMessage> temp = ChooseIn(rmList, lLongitude + lob * i, lLongitude + lob * (i + 1), dLatitude + lab * j, dLatitude + lab * (j + 1), "1");
				blockMessages.add(temp);
				ArrayList<RoughMessage> temp1 = ChooseIn(rmList, lLongitude + lob * i, lLongitude + lob * (i + 1), dLatitude + lab * j, dLatitude + lab * (j + 1), "2");
				blockMessages.add(temp1);
				ArrayList<RoughMessage> temp2 = ChooseIn(rmList, lLongitude + lob * i, lLongitude + lob * (i + 1), dLatitude + lab * j, dLatitude + lab * (j + 1), "3");
				blockMessages.add(temp2);
				ArrayList<RoughMessage> temp3 = ChooseIn(rmList, lLongitude + lob * i, lLongitude + lob * (i + 1), dLatitude + lab * j, dLatitude + lab * (j + 1), "4");
				blockMessages.add(temp3);
			}
		}

		rmList.clear();
		return blockMessages;
	}

	// auxiliary function to choose messages in a block.
	public static ArrayList<RoughMessage> ChooseIn(
			ArrayList<RoughMessage> msgList, double l, double r, double d,
			double u, String category) {
		ArrayList<RoughMessage> temp = new ArrayList<RoughMessage>();
		int size = msgList.size();
		for (int i = 0; i < size; i++) {
			
//			System.out.println("the Longitude of " + i + " is " + msgList.get(i).getLongitudeExist());
//			System.out.println("the Latitude of " + i + " is " + msgList.get(i).getLatitudeExist());
			if (msgList.get(i).getLatitudeExist() > d
					&& msgList.get(i).getLongitudeExist() < r
					&& msgList.get(i).getLatitudeExist() < u
					&& msgList.get(i).getLongitudeExist() > l
					&& msgList.get(i).getIdCategory().equals(category)) {
				temp.add(msgList.get(i));
				
				if (msgList.get(i).getLatitudeExist() > 34
						&& msgList.get(i).getLongitudeExist() < 106
						&& msgList.get(i).getLatitudeExist() < 35
						&& msgList.get(i).getLongitudeExist() > 109){
					System.out.println("Xian's note is in them");
				}
			}
		}
//		System.out.println(temp.size());
		return temp;
	}
}
