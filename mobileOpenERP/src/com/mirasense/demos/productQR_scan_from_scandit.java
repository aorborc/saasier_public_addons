package com.mirasense.demos;

import java.util.List;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.mirasense.scanditsdk.ScanditSDKAutoAdjustingBarcodePicker;
import com.mirasense.scanditsdk.interfaces.ScanditSDK;
import com.mirasense.scanditsdk.interfaces.ScanditSDKListener;
import com.openerp.MainActivity;
import com.openerp.R;
import com.openerp.addons.idea.Dash_Board;
import com.openerp.addons.idea.Product_Detail;
import com.openerp.addons.idea.QR_Equipment;
import com.openerp.addons.idea.partners;
import com.openerp.addons.idea.product;
import com.openerp.addons.idea.work_orders;
import com.openerp.base.account.AccountFragment;
import com.openerp.orm.OEHelper;
import com.openerp.support.BaseFragment;
import com.openerp.support.fragment.FragmentListener;
import com.openerp.util.drawer.DrawerItem;

/**
 * Simple demo application illustrating the use of the Scandit SDK.
 * 
 * Important information for the developer with respect to Android 2.1 support!
 * 
 * Android 2.1 differs from subsequent versions of Android OS in that it does
 * not offer a camera preview mode in portrait mode (landscape only). Android
 * 2.2+ offers both - a camera preview in landscape mode and in portrait mode.
 * There are certain devices that run Android 2.2+ but do not properly implement
 * the methods needed for a portrait camera view.
 * 
 * To address this difference between the Android versions, the Scandit SDK
 * offers the following approaches and the developer needs to choose his
 * preferred option:
 * 
 * If you are showing the scanner on the full screen in a new Activity:
 * 
 * - Instantiate the ScanditSDKAutoAdjustingBarcodePicker which will choose
 * whether to use the new or legacy picker.
 * 
 * If you want to show the picker inside a view hierarchy/cropped/scaled you
 * have to make the distinction between the different pickers yourself. Fore
 * devices that don't support the new picker the following options exist:
 * 
 * - a scan view in landscape mode scanning only(!) that is fully customizable
 * by the developer - ScanditSDKBarcodePicker.class
 * 
 * - our own custom scan view with portrait mode scanning that offers only
 * limited customization options (show/hide title & tool bars, but no additional
 * Android UI elements) - LegacyPortraitScanditSDKBarcodePicker.class
 * 
 * For devices that do support the new picker the following options exist:
 * 
 * - a scan view with portrait mode scanning that is fully customizable by the
 * developer (RECOMMENDED) - ScanditSDKBarcodePicker.class
 * 
 * - any of the options listed under Android 2.1
 * 
 * We recommend that developers choose the scan view in portrait mode on Android
 * 2.2. It has the native Android look&feel and provides full customization. We
 * provide our own custom scan view
 * (LegacyPortraitScanditSDKBarcodePicker.class) in Android 2.1 to provide
 * backwards compatibility with Android 2.1.
 * 
 * To integrate the Scandit SDK, carry out the following three steps:
 * 
 * 1. Create a BarcodePicker object that manages camera access and bar code
 * scanning:
 * 
 * e.g. ScanditSDKBarcodePicker barcodePicker = new
 * ScanditSDKBarcodePicker(this, R.raw.class, "your app key", true,
 * ScanditSDKBarcodePicker.LOCATION_PROVIDED_BY_SCANDIT_SDK);
 * 
 * IMPORTANT: Make sure add your app key here. It is available from your Scandit
 * SDK account.
 * 
 * 2. Add it to the activity: my_activity.setContentView(barcodePicker);
 * 
 * 3. Implement the ScanditSDKListener interface (didCancel, didScanBarcode,
 * didManualSearch) and register with the ScanditSDKOverlayView to receive
 * callbacks: barcodePicker.getOverlayView().addListener(this);
 * 
 * 
 * If you want to use the custom scan view for scanning in portrait mode in
 * Android 2.1, instantiate the LegacyPortraitScanditSDKBarcodePicker class (as
 * shown in the example below). There is utility method available to determine
 * whenever the default portrait scan view is not available
 * ScanditSDKBarcodePicker.canRunPortraitPicker().
 * 
 * 
 * 
 * Copyright 2010 Mirasense AG
 */

/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied See the
 * License for the specific language governing premissions and limitations under
 * the License.
 */
public class productQR_scan_from_scandit extends BaseFragment implements
		ScanditSDKListener {

	// The main object for recognizing a displaying barcodes.
	private ScanditSDK mBarcodePicker;
	int indexofbarcodeproduct = 0;
	String EAN13_code = null;// //a9J2Fu/JEeOSwdn9NTRuk2N79KHBTzTQMAUChbxWC/4
	OEHelper oeh;

	// Enter your Scandit SDK App key here.
	// Your Scandit SDK App key is available via your Scandit SDK web account.
	public static final String sScanditSdkAppKey = "a9J2Fu/JEeOSwdn9NTRuk2N79KHBTzTQMAUChbxWC/4";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);

		MainActivity.global = 2;
		// Toast.makeText(getActivity(), "call codebar", 30).show();

		oeh = new OEHelper(getActivity());
		// initializeAndStartBarcodeScanning();

		// View rootView = inflater.inflate(R.layout.main, container,
		// false);

		// getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getActivity().getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// We instantiate the automatically adjusting barcode picker that will
		// choose the correct picker to instantiate. Be aware that this picker
		// should only be instantiated if the picker is shown full screen as the
		// legacy picker will rotate the orientation and not properly work in
		// non-fullscreen.
		ScanditSDKAutoAdjustingBarcodePicker picker = new ScanditSDKAutoAdjustingBarcodePicker(
				getActivity(), sScanditSdkAppKey,
				ScanditSDKAutoAdjustingBarcodePicker.CAMERA_FACING_BACK);

		// Add both views to activity, with the scan GUI on top.
		// setContentView(picker);
		// container.removeView(picker);
		// container.addView(picker);
		// setContentView(picker);
		mBarcodePicker = picker;

		// Register listener, in order to be notified about relevant events
		// (e.g. a successfully scanned bar code).
		mBarcodePicker.getOverlayView().addListener(this);

		// Show a search bar in the scan user interface.
		mBarcodePicker.getOverlayView().showSearchBar(true);

		return picker;

		// return rootView;

	}

	@Override
	public void onPause() {
		// When the activity is in the background immediately stop the
		// scanning to save resources and free the camera.
		mBarcodePicker.stopScanning();

		super.onPause();
	}

	@Override
	public void onResume() {
		// Once the activity is in the foreground again, restart scanning.
		mBarcodePicker.startScanning();
		super.onResume();
	}

	/**
	 * Initializes and starts the bar code scanning.
	 */
	// public void initializeAndStartBarcodeScanning() {
	// // Switch to full screen.
	//
	// }

	/**
	 * Called when a barcode has been decoded successfully.
	 * 
	 * @param barcode
	 *            Scanned barcode content.
	 * @param symbology
	 *            Scanned barcode symbology.
	 */
	public void didScanBarcode(String barcode, String symbology) {
		// Remove non-relevant characters that might be displayed as rectangles
		// on some devices. Be aware that you normally do not need to do this.
		// Only special GS1 code formats contain such characters.
		String cleanedBarcode = "";
		for (int i = 0; i < barcode.length(); i++) {
			if (barcode.charAt(i) > 30) {
				cleanedBarcode += barcode.charAt(i);
			}
		}

		// ===============================================================================
		// EAN13_code=cleanedBarcode;

		// Toast.makeText(getActivity(), "QR Code = "+cleanedBarcode,
		// 30).show();
		// ===============================================================================
		// EAN13_code=cleanedBarcode;
		String selected = cleanedBarcode;

		Product_Detail detail = new Product_Detail();

		Bundle args = new Bundle();

		if (OEHelper.ean13_of_product_product.contains(selected)) {

			if (OEHelper.ean13_of_product_product.size() != 0) {
				indexofbarcodeproduct = OEHelper.ean13_of_product_product
						.indexOf(selected);
			}
			if (OEHelper.list_price_of_product_template.size() != 0) {
				String sale_price_of_product_template = OEHelper.list_price_of_product_template
						.get(indexofbarcodeproduct);
				args.putString("saleprice", sale_price_of_product_template);
			}
			if (OEHelper.standard_price_of_product_template.size() != 0) {
				String cost_price_of_product_template = OEHelper.standard_price_of_product_template
						.get(indexofbarcodeproduct);
				args.putString("costprice", cost_price_of_product_template);
			}
			if (OEHelper.ean13_of_product_product.size() != 0) {
				String ean13_of_product_product1 = OEHelper.ean13_of_product_product
						.get(indexofbarcodeproduct);
				args.putString("ean13", ean13_of_product_product1);
			}
			if (OEHelper.default_code_of_product_product.size() != 0) {
				String reference_of_product_product1 = OEHelper.default_code_of_product_product
						.get(indexofbarcodeproduct);
				args.putString("reference", reference_of_product_product1);
			}
			if (OEHelper.type_of_product_template.size() != 0) {
				String type_Of_product_template = OEHelper.type_of_product_template
						.get(indexofbarcodeproduct);
				args.putString("type", type_Of_product_template);
			}
			if (OEHelper.supply_method_product_template.size() != 0) {
				String supply_method_product_template1 = OEHelper.supply_method_product_template
						.get(indexofbarcodeproduct);
				args.putString("supplymethod", supply_method_product_template1);
			}
			if (OEHelper.procure_method_product_template.size() != 0) {
				String procure_method_product_template1 = OEHelper.procure_method_product_template
						.get(indexofbarcodeproduct);
				args.putString("procuremethod",
						procure_method_product_template1);
			}

			// args.putParcelable("BundleIcon", image_of_produc1t);
			if (OEHelper.datatemplate.size() != 0) {
				args.putString("name",
						OEHelper.datatemplate.get(indexofbarcodeproduct));
			}
			
			if (OEHelper.uom_product_product.size() != 0) {
				args.putString("uom", OEHelper.uom_product_product
						.get(indexofbarcodeproduct));
			}
			Toast.makeText(getActivity(), "EAN13_code matched", 90).show();

			detail.setArguments(args);
			FragmentListener frag = (FragmentListener) getActivity();
			frag.startDetailFragment(detail);

			// getSupportFragmentManager().beginTransaction()
			// .add(android.R.id.content, detail).commit();

			// work_orders firstFragment = new work_orders();
			// getSupportFragmentManager().beginTransaction()
			// .add(R.id.fragment_container, firstFragment).commit();
		} else {
			Toast.makeText(getActivity(), "code not matched", 90).show();
		}

		// ================================================================================
	}

	/**
	 * Called when the user entered a bar code manually.
	 * 
	 * @param entry
	 *            The information entered by the user.
	 */
	public void didManualSearch(String entry) {
		// Toast.makeText(this, "User entered: " + entry,
		// Toast.LENGTH_LONG).show();
	}

	@Override
	public void didCancel() {
		mBarcodePicker.stopScanning();
		// finish();
	}

	@Override
	public Object databaseHelper(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DrawerItem> drawerMenus(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

}
