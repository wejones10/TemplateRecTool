package templaterectool;

/*
 * @author bjones - the fourteenth Warrior
 * 
 * A couple of notes for help:
 * 
 * Descriptions of items and objects used:
 * JPanel - a panel in a GUI. Easiest to think of as a rectangle. You can add multiple objects to a panel, including other panels. By default, all objects will inherit their width from the panel to which they're added.
 *          To avoid having all smaller objects stretched out to match the largest object, it is best to put smaller objects in their own
 *          panels, and then add those panels to the main panel. There is no limit to the number of iterations you do like this (panel within a panel within a panel...).
 *          Objects can be sized, centered, etc. but these are dependent on the layout chosen for the panel. Each panel can use a different layout.   ***See below for layout explanations***
 * JLabel - A panel that holds text.
 * JComboBox - a combo box that acts as a drop-down list. The default width is equal to the widest string in the combo box, but will inherit the width of the widest object in the panel its in when
 *             the combo box gets added to a panel. Items are added as strings or an array of strings. You can connect an ItemListener method class to determine which item in the drop-down was selected by the user.
 * JList - a list of text items with a white background. Items are added as strings or an array of strings. You can set if user's can select one or more items from the list at a time. 
 *         The height and width is automatically determined by the number of items in the list, and the longest item in the list. If put in a JScrollPane, you can set the minimum number of items
 *         that show. If not added to a scroll pane, and the list is taller or wider than the JPanel the list is beign added to, this will stretch the JPanel the list is in.
 *         You can add a ListSelectionListener class to determine which item in the list was selected by the user. In the ListSelectionListener, you can reference the string chosen by the string or by it string's 
 *         order in the list (called its index, which starts at 0).
 * JScrollPane - a pane that can scroll horizontally or vertically. You can set them to scroll as needed, always, or never, independently for both directions. Typically, long JLists
 *               are added to these so the list size is managable, the panel the list is in doesn't grow too large, and the use can scroll through the items.
 * String nameOfString [] - an array of strings, where each item is placed in its own set of quotation marks and each item is separated by a comma outside of the quotes
 * 
 * Layouts used by JPanels in this app - layout types determine the appearance of the objects within the JPanel.
 * FlowLayout - Objects get added to the JPanel like text (left to right by default), in the order they are added to the panel. This is the default layout unless otherwise stated.
 * CardLayout - Objects in the JPanel typicall show one at a time, and change based on user input. Panels using this layout are called cards. 
 *              Ex. makes one list appear at a time, and the list's contents change based on item chosen from a combo box.
 * TabLayout - JPanel appear with tabs on the top. Selecting different tabs will cause different Jpanels to appear.
 * BoxLayout - Objects get added to the JPanel either vertically (top to bottom) or horizontally (left to right), in the order they are added in the code. Boxlayout.Y_AXIS lays them out vertically.
 *             You can also add rigidArea objects with specific sizes to create blank spaces vertically or horizontally between other objects. This helps size and space objects to look good.
 * 
 * Adding one object to another:
 * To add a component object to a parent object, use parentObjectName.add(componentObjectName); Ex. to ad JList regionList to the JPanel regionPanel, use regionPanel.add(regionList); 
 * Typically, any modifications to the object, such as centering, setting the size, etc., have to be done before adding the component to the parent panel
 * You can add any object to a panel (including other panels with its objects). You can add a JList to a JScrollBar. 
 * 
 * Getting user input:
 * To get user input, connect ItemListeners or ActionListeners classes to objects. Each individual ItemListener or ActionListener class is located at the bottom of the code
 * 
 *Design notes:
 * In general, I added every list to a JScroll Pane even if the list contained few items. This is so that if the list grows in the future, it will easily scale. This also enabled
 * the application to show the same-sized lists of 12 items for every list by using the .setVisibleRowCount method. Some lists show fewer rows if size did not permit all 12.
 * I also tried to keep every object its own natural size, so that a combo-box would not artificially stretch horizontally to fit within the panel. To do this, I added objects
 * to their own panels, and then added that panel to the larger parent panel.
 * 
 *
 * v 1.1 - Templates 19.0
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.UIManager.*;

public class TemplateRecTool extends JFrame implements ItemListener, ActionListener {

    //names of main tabs variables
    final static String ACCRUALS = "    ACCRUALS    ";
    final static String EXCEPTIONS = "    EXCEPTIONS    ";
    final static String SHIFT_PAY = "    SHIFT PAY    ";
    final static String OVERTIME = "    OVERTIME    ";
    final static String REGULATIONS = "    REGULATIONS    ";
    JTabbedPane tabbedPane; //pane that uses TabLayout. Each tab fits in this parent container, but each tab must be its own panel.
    
    
    //***Accruals Tab variables************************
    JPanel accrualsTab;
    JPanel accrualCards; //panel that uses card layout for accruals tab
    JLabel accrualDirections = new JLabel("Use the dropdown to choose a type of accrual template, then click an the item in the list");  //directions for entire accrual tab
    
    //choices presented in accruals tab's combo box
    final static String accrualType1 = "Banks with accruals";
    final static String accrualType2 = "Banks without accruals";
    final static String accrualType3 = "Miscellaneous pieces";
    String accrComboBoxItems[] = {accrualType1, accrualType2, accrualType3};  //puts string choices into the combo box
    
    //list presented if accrualType1 is chosen from comboBox
    JList withAccrualsList;
    String[] withAccrualsOptions = { "Attendance Points",  "---------------------------------------------------",
        "Allows employees to convert OT to PTO", "---------------------------------------------------", 
        "Annual accrual with a new hire accrual", 
        "Annual accrual with a new hire accrual and a separate carryover bank",
        "Monthly accrual", 
        "Semi-monthly accrual - accrues at beginning of period", 
        "Semi-monthly accrual - accrues at end of period", 
        "Bi-weekly accrual - accrues at beginning of period", 
        "Bi-weekly accrual - accrues at end of period", 
        "Weekly accrual - accrues at beginning of week", 
        "Weekly accrual - accrues at end of week" };
    
    //list presented if accrualType2 is chosen from combobox
    JList withoutAccrualsList;
    String withoutAccrualsOptions[] = {"Basic bank, accrual for adjustments, empty clearing, and \"bank exhausted\" exception",
        "Basic bank, separate carryover bank, accruals for adjustments, clearings, and \"bank exhausted\" exception"};
    
    //list presented if accrualType3 is chosen from combobox
    JList miscAccrualsList;
    String miscAccrualsOptions[] = {"Clears all hours not used after x days",
        "Accrues all hours used x days ago", 
        "Exception if employee has used more time than will be able to accrue by year end",
        "Exception if too many days in a row are absences when compared with the employee's schedule", 
        "Exception if too many days in a row are absences between Monday and Friday"};

    
    //***Exceptions Tab variables************************
    JPanel exceptionsTab;
    JLabel exceptionsDirections = new JLabel("Choose among the following items in the list");  //directions for entire exceptions tab
    JPanel exceptionsListPanel;
    JList exceptionsList;
    String exceptionsListOptions[] = {"Blank exception template with exception code, policy, and trigger",
        "----------Employee absence----------", "Absent pay code used on an unscheduled day", 
        "Too many days in a row are absences when compared with the employee's schedule", 
        "Too many days in a row are absences between Monday and Friday",
        "No work time on scheduled day", 
        "--------------Employee early or late----------", "Employee arrived early or late", 
        "Employee left early or late", 
        "----------Employee didn't work expected hours----------", "Employee worked less than scheduled hours on a day", 
        "Employee worked less than standard daily hours on a day", 
        "Employee worked less than standard weekly hours in a week", 
        "Employee worked more than scheduled hours on a day", 
        "Employee worked more than standard daily hours in a day", 
        "Employee worked more than standard weekly hours in a week",
        "Employee worked on an unscheduled day",
        "----------Pay code behavior----------", "Pay code entered in incorrect increment", 
        "Pay code that is restricted from being used x times per year has been used too many times",
        "----------Mobile restrictions----------", "Employee punched in on mobile device outside of acceptable geofence from the business",
        "Employee punched in on mobile device outside of acceptable radius from the business",
        "----------Bank----------", "Employee's bank has gone negative and employee has used more banked time than the employee will be able to accrue by year end",
        "----------Time-Off Requests----------", "Exceptions included in a list should only show on the Time Off Request page and not on a timesheet"};
   
    
    //***Shift Pay Tab variables**********************
    JPanel shiftPayTab;
    JLabel shiftPayDirections = new JLabel("Choose one option from all available lists");  //directions for entire shift pay tab
    JPanel shiftPayAllListsPanel; //panel that holds all three lists together and keeps them evenly spaced
    JPanel shiftPayDirectionsAllListsPanel; //panel that holds directions and above panel together, to keep each centered
    
    //panel with first list in shift pay tab
    JPanel shiftResultsPanel;
    JLabel shiftResultsDirections = new JLabel("How should the shift diff appear on the Results Tab?");  //directions for 1st shift diff list
    JList resultsList;
    String resultsOptions[] = {"Work is reclassified to a shift diff pay code",  
        "Shift pay is transacted with a shift diff pay code", 
        "Work with shift pay is marked with an indicator"};
    JButton resultsHelpButton; //help button that explains how each choice in the list looks on the results tab
    
    //panel with second list in shift pay tab
    JPanel shiftDayPanel;
    JLabel shiftDayDirections = new JLabel("For this transacted shift diff, how many shift diff pay codes will be used?");  //directions for 2nd shift diff list
    JList dayList;
    String dayOptions[] = {"output one pay code for a single rate - combine all shift pay into one row per day",  //choices in 2nd shift diff list
        "output multiple pay codes for multiple rates - keep all shift pay separated"};
    JButton dayHelpButton;
    
    //panel with third list in shift pay tab
    JLabel shiftMethodDirections = new JLabel("What information is used to determines if shift diff is earned?");  //directions for 3rd shift diff list
    JPanel shiftMethodPanel;
    JList methodList;
    String methodOptions[] = {"Shift starts within a window",
        "Shift ends within a window", 
        "Shift falls outside of schedule", 
        "Shift portion that crosses window", 
        "Entire shift if 50% or more of window is crossed                   ",  //extra space at end extends the list's width horizontally to fill the panel it's in
        "Employee or Assignment record information", 
        "LD information from time record"};
    
    //store portion of shift diff template name already decided from panel 1 and panel 2 until the final result is chosen
    private String shiftOutput1 = "";
    private String shiftOutput2 = "";
    
    
    //***Overtime Tab variables**********************
    JPanel overtimeTab;
    JPanel overtimeCards; //panel that uses card layout for overtime tab
    JLabel overtimeDirections = new JLabel("Use the dropdown to choose the length of time over which overtime is measured, or if the overtime is based on employee rest periods, then click an item in the list");  //directions for entire overtime tab
    
    //overtime context choices presented in overtime tab's combo box
    final static String context1 = "Weekly or multiple-days (including every xth day)";            
    final static String context2 = "Daily or less than 24 hours (including rolling days)";
    final static String context3 = "Employee's rest";
    String contextOptions[] = { context1, context2, context3};  //puts string choices into the combo box
    
    //list presented if context1 is chosen from comboBox
    JList weeklyList;
    String weeklyContextOptions[] = {"Use your configuration's week definition",  //choices in weekly OT list
        "Use a week with configurable week begin and week end days", 
        "Use a configurable number of days within a week or period", 
        "For working on the xth day, and all following days until employee has a day off",
        "If weekly worked hours are greater than weekly scheduled hours", 
        "For working over consecutively unscheduled days", 
        "For every 7th day of the week", 
        "For every xth consecutive day (Ex. every 5th day - day 5, 10, 15, etc.)", 
        "For a weekly overtime rule that operates over a week that is split into two periods",
        "For a traditional 9/80 schedule where an employee works 9 days and 80 hours over two weeks"};
    
    //list presented if context2 is chosen from comboBox
    JList dailyList;
    String dailyContextOptions[] = {"Use your configuration's day definition",  //choices in daily OT list
        "Use a rolling day definition", 
        "Set the start and end time of a 24-hour day",
        "For working greater than a configurable number of consecutive hours within a shift",
        "For working greater than a configurable number of hours, within a window with a configurable start and end time",
        "If the employee worked time outside of schedule, on a scheduled day", 
        "If total worked hours > total scheduled hours regardless of schedule overlap", 
        "For working an unscheduled day",};
    
    //list presented if context3 is chosen from comboBox
    JList restList;
    String restContextOptions[] = {"Reclassifies entire second shift if not enough rest came between shifts", //choices in employee-rest-based OT list
        "Reclassify only portion of second shift that overlaps rest break",
        "Transacts a slice equal to the missed rest amount", 
        "Transacts a slice for a set number of hours", 
        "Transacts a slice equal to the second shift's length",
        "Transacts a slice equal to the portion of second shift that overlaps rest break"};

    
    //***Regulations Tab variables**********************
    JPanel regulationsTab;
    JPanel regionCards; //panel that uses card layout for regulations tab
    JLabel regulationDirections = new JLabel("Use the dropdown to choose the global region needed");
    //context choices used in regulations dropdown
    final static String region1 = "U.S.A.";
    final static String region2 = "Canada";
    final static String region3 = "European Union (EU)";
    String regComboBoxItems[] = { region1, region2, region3 }; //puts string choices into the combo box
    
    //choices after choosing U.S.A. region
    JPanel region1Cards;  //generic GUI panel that uses card layout, which switches appearance based on drop-down choice
    JLabel region1Directions = new JLabel("Use the dropdown to choose the either among the federal level, state, or territory in which the business operates, then choose an option from the list");
    final static String region1Option1= "Federal";
    final static String region1Option2 = "Alaska (AK)";
    final static String region1Option3 = "California (CA)";
    final static String region1Option4 = "Colorado (CO)";
    final static String region1Option5 = "Connecticut (CT)";
    final static String region1Option6 = "Kansas (KS)";
    final static String region1Option7 = "Kentucky (KY)";
    final static String region1Option8 = "Massachusetts (MA)";
    final static String region1Option9 = "Minnesota (MN)";
    final static String region1Option10 = "Nevada (NV)";
    final static String region1Option11 = "New Jersey (NJ)";
    final static String region1Option12 = "New York (NY)";
    final static String region1Option13 = "Oregon (OR)";
    final static String region1Option14 = "Pennsylvania (PA)";
    final static String region1Option15 = "Puerto Rico (PR)";
    final static String region1Option16 = "Rhode Island (RI)";
    final static String region1Option17 = "Washington (WA)";
    final static String region1Option18 = "Washington, D.C. (DC)";
    final static String region1Option19 = "Wisconsin (WI)";
    String region1ComboBoxItems[] = { region1Option1, region1Option2, region1Option3, region1Option4, region1Option5, region1Option6, region1Option7, region1Option8, 
        region1Option9, region1Option10, region1Option11, region1Option12, region1Option13, region1Option14, region1Option15, region1Option16, region1Option17, 
        region1Option18, region1Option19 };  //puts string choices into the combo box
    JList region1FederalList;
    String region1FederalOptions[] = {"FLSA weekly - creates OT adjustment slices", 
        "FLSA - adjusts OT that was already created",
        "FMLA - banks based on the First Use rule",
        "FMLA - Military banks based on the First Use rule",
        "FMLA - banks based on rolling rule", 
        "FMLA - banks based on  yearly rule", 
        "FMLA - banks based on fixed date rule"};
    JList region1AKList;
    String region1AKOptions[] = {"State-regulated Overtime"}; 
    JList region1CAList;
    String region1CAOptions[] = {"State-regulated Overtime",
        "State-regulated Overtime for alternate schedule",
        "State-regulated sick accrual",
        "Make-up time",
        "Farmily Rights Act for employee leave",
        "California Meal - generates slice when exception is acknowledged",
        "California Meal - generates slice, removed when exception is acknowledged",
        "Rest breaks that must occur during a shift",
        "Oakland city-regulated sick accrual",
        "San Diego city-regulated sick accrual",
        "San Francisco city-regulated sick accrual"};
    JList region1COList;
    String region1COOptions[] = {"State-regulated Overtime"}; 
    JList region1CTList;
    String region1CTOptions[] = {"State-regulated sick accrual"};
    JList region1KSList;
    String region1KSOptions[] = {"State-regulated Overtime"}; 
    JList region1KYList;
    String region1KYOptions[] = {"State-regulated Overtime"}; 
    JList region1MAList;
    String region1MAOptions[] = {"State-regulated sick accrual"}; 
    JList region1MNList;
    String region1MNOptions[] = {"State-regulated Overtime"}; 
    JList region1NVList;
    String region1NVOptions[] = {"State-regulated Overtime"}; 
    JList region1NJList;
    String region1NJOptions[] = {"Jersey City-regulated sick accrual",
        "Newark city-regulated sick accrual"}; 
    JList region1NYList;
    String region1NYOptions[] = {"New York City-regulated sick accrual", "New York Meal"}; 
    JList region1ORList;
    String region1OROptions[] = {"Eugene city-regulated sick accrual", "Portland city-regulated sick accrual"}; 
    JList region1PAList;
    String region1PAOptions[] = {"Philadelphia city-regulated sick accrual"}; 
    JList region1PRList;
    String region1PROptions[] = {"Territory-regulated Overtime",
        "Puerto Rico Meal"};
    JList region1RIList;
    String region1RIOptions[] = {"State-regulated Overtime"}; 
    JList region1WAList;
    String region1WAOptions[] = {"Seattle city-regulated sick accrual"};
    JList region1DCList;
    String region1DCOptions[] = {"District-regulated sick accrual"};
    JList region1WIList;
    String region1WIOptions[] = {"One day off in seven days"};
    boolean isChanging = false;  //monitors if a list's value has changed, will be used to clear all other lists where a selection may have been made

    //choices after choosing Canada region
    JPanel region2Cards;  //generic GUI panel that uses card layout, which switches appearance based on drop-down choice
    JLabel region2Directions = new JLabel("Use the dropdown to choose among the federal level, province or territory in which the business operates, then choose an option from the list");
    final static String region2Option1 = "Federal workers";
    final static String region2Option2 = "Alberta";
    final static String region2Option3 = "British Columbia";
    final static String region2Option4 = "Manitoba";
    final static String region2Option5 = "New Brunswick";
    final static String region2Option6 = "Newfoundland and Labrador";
    final static String region2Option7 = "Northwest Territories";
    final static String region2Option8 = "Nova Scotia";
    final static String region2Option9 = "Nunavut";
    final static String region2Option10 = "Ontario";
    final static String region2Option11 = "Prince Edward Island";
    final static String region2Option12 = "Quebec";
    final static String region2Option13 = "Yukon";
    final static String region2Option14 = "Saskatchewan";
    String region2ComboBoxItems[] = { region2Option1, region2Option2, region2Option3, region2Option4, region2Option5, region2Option6, region2Option7, region2Option8,
        region2Option9, region2Option10, region2Option11, region2Option12, region2Option13, region2Option14 };  //puts string choices into combo box
    JList region2List;
    String region2CardOptions[] = {"Regulated overtime", "Regulated holiday pay and/or premium for working on a holiday"};
    final static String region2Choice1 = "Overtime";  //drop-down option 1 after choosing prov/territory
    final static String region2Choice2 = "Holiday Pay";  //drop-down option 2 after choosing prov/territory
    private String region2OutputAppendix = "LAWS_OT";  //appends choice with either _HOL_PAY or _LAWS_OT
    String reg2Choice;  //records choice made in provincial/territory drop-down so I don't have to create 14 lists and classes with the same information
    String region2OutputPrefix = "CAN_FED_";  //the output of the choice made in provincial/territory drop-down
    
    //choices after choosing UK/EU region
    JList region3List;
    JLabel region3Directions = new JLabel("Choose an option from the list");
    String region3CardOptions[] = {"Annual Hours (EU)",  //choices for EU regulations
        "Flexi Time using I/O time (EU)", 
        "Flexi Time using EL time (EU)", 
        "Driving Rest for Adults (EU)", "---Working Time Directive for Adults---", 
        "Working Time Directive - Rest Breaks during day (UK)", 
        "Working Time Directive - Daily Rest (UK)", 
        "Working Time Directive - Weekly Rest (UK)", 
        "Working Time Directive - maximum weekly working time using fixed period (UK)", 
        "Working Time Directive - maximum weekly working time using rolling period (UK)", 
        "Working Time Directive - night and/or hazardous work", "---Working Time Directive for Minors---", 
        "Working Time Directive - Rest Breaks during day (UK)", 
        "Working Time Directive - Daily Rest (UK)", 
        "Working Time Directive - Weekly Rest (UK)", 
        "Working Time Directive - maximum weekly working time (UK)"};
 
    
    //***Output Panel variables************************
    JLabel outputLabel = new JLabel();
    String recommendOutput;
    JLabel recLabel;
    JLabel recOutputLabel;
    final static String recOutputPlaceholder = "Please choose a template";  //words placed in label until a template has been selected
    int listIndex;  //int that stores choice made from each list
    
    
    public void addComponentToPane(Container pane) {  //class that describes how GUI is put together by its various components
        
        tabbedPane = new JTabbedPane(); //main parent panel to hold accruals, shift pay, overtime, and regulations tabs
        
        
        //*****create Accruals Tab*****
        accrualsTab = new JPanel(); //panel for Accruals Tab
        accrualsTab.setLayout(new BoxLayout(accrualsTab, BoxLayout.Y_AXIS));  //sets layout of objects in accrualsTab. orders objects top to bottom
        
        JPanel accComboBoxWithDirectionsPanel = new JPanel();  //new panel to hold combo box and directions. Multiple panels makes objects easier to size and center
        accComboBoxWithDirectionsPanel.setLayout(new BoxLayout(accComboBoxWithDirectionsPanel, BoxLayout.Y_AXIS));  //give accComboBoxwithDirectionsPanel vertical layout and orders objects top to bottom
        accComboBoxWithDirectionsPanel.add(Box.createRigidArea(new Dimension(0, 15)));  //creates space between edge of panel and directions
        accrualDirections.setAlignmentX(CENTER_ALIGNMENT);  //center directions when they get put into a panel. Must be done before adding to panel
        accComboBoxWithDirectionsPanel.add(accrualDirections);  //add directions as first object in accComboBoxwithDirectionsPanel
        
        JPanel accComboBoxPanel = new JPanel();  //new panel to only hold combo box which means combo box will only be as large as it needs to be
        JComboBox accComboBox = new JComboBox<>(accrComboBoxItems);  //create combo box and add items items from array as options - <> allows object to infer passed parameter is strings
        accComboBox.setEditable(false);  //makes combo box un-editable, so user can't type new items in when program is run
        accComboBox.addItemListener(this);  //connects itemListener class to the combo box, so we can read the user's input
        accComboBox.setAlignmentX(CENTER_ALIGNMENT);  //center combo box when it gets added to a panel. Must be done before adding to panel.
        accComboBoxPanel.add(accComboBox);  //add combo box to panel that only holds combo box. Now accComboBoxPanel is complete.
        
        accComboBoxWithDirectionsPanel.add(accComboBoxPanel);  //add panel with combo box to panel, under directions. Now accComboBoxWithDirectionsPanel is complete.
        accrualsTab.add(accComboBoxWithDirectionsPanel);  //add panel with directions and combo box to the tab

            //create cards that will show different lists depending on user's drop down choice
            //create withAccruals card
            JPanel withAccrualsCard = new JPanel();  //card
            withAccrualsList = new JList<>(withAccrualsOptions); //create list and add withAccrualsOptions array to it - <> allows object to infer passed parameter is strings
            withAccrualsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //only one item from list can be selected at a time
            withAccrualsList.addListSelectionListener(new withAccrualsSelectionHandler());  //connects ListSelectionListener to the list
            withAccrualsList.setVisibleRowCount(13);  //standard row count in app is 12, but it seems stupid to scroll for only one more row
            JScrollPane withAccrualsListScrollPane = new JScrollPane(withAccrualsList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);  //will scroll vertically only if list grows > 13
            withAccrualsListScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
            withAccrualsCard.add(withAccrualsListScrollPane);  //add list to withAccruals card
        
            //create withoutAccruals card
            JPanel withoutAccrualsCard = new JPanel();  //card
            withoutAccrualsList = new JList<>(withoutAccrualsOptions);  //create list and add withoutAccrualsOptions array to it
            withoutAccrualsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            withoutAccrualsList.addListSelectionListener(new withoutAccrualsSelectionHandler());
            withoutAccrualsList.setVisibleRowCount(12);
            JScrollPane withoutAccrualsListScrollPane = new JScrollPane(withoutAccrualsList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            withoutAccrualsListScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
            withoutAccrualsCard.add(withoutAccrualsListScrollPane);
            
            //create misc accruals card
            JPanel miscAccrualsCard = new JPanel();  //card
            miscAccrualsList = new JList<>(miscAccrualsOptions);  //create list and add miscAccrualsOptions array to it
            miscAccrualsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            miscAccrualsList.addListSelectionListener(new miscAccrualsSelectionHandler());
            miscAccrualsList.setVisibleRowCount(12);
            JScrollPane miscAccrualsListScrollPane = new JScrollPane(miscAccrualsList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            miscAccrualsListScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
            miscAccrualsCard.add(miscAccrualsListScrollPane);
            
            //associate each card with an item in the Accruals Tab combo box
            accrualCards = new JPanel(new CardLayout());
            accrualCards.add(withAccrualsCard, accrualType1);
            accrualCards.add(withoutAccrualsCard, accrualType2);
            accrualCards.add(miscAccrualsCard, accrualType3);
        
        accrualsTab.add(accrualCards);  //add cards to the Accruals Tab
        //to finish, add Accruals Tab to tab panel
        tabbedPane.add(ACCRUALS, accrualsTab);
        
        
        //*****create Exceptions Tab*****
        exceptionsTab = new JPanel();  //panel for Exceptions tab
        exceptionsListPanel = new JPanel();
        exceptionsListPanel.setLayout(new BoxLayout(exceptionsListPanel, BoxLayout.Y_AXIS));  //give accComboBoxwithDirectionsPanel vertical layout and orders objects top to bottom
        exceptionsListPanel.add(Box.createRigidArea(new Dimension(0, 10)));  //creates space between edge of panel and directions
        exceptionsDirections.setAlignmentX(CENTER_ALIGNMENT);  //center exceptions directions then they get put in the panel
        exceptionsListPanel.add(exceptionsDirections);
        exceptionsListPanel.add(Box.createRigidArea(new Dimension(0, 10)));  //creates space between directions and list
        exceptionsList = new JList<>(exceptionsListOptions);  //create list and add exceptionsListOptions array to it
        exceptionsList.setAlignmentX(CENTER_ALIGNMENT);
        exceptionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //only one item from list can be selected at a time
        exceptionsList.addListSelectionListener(new exceptionsSelectionHandler());  //connects ListSelectionListener to the list
        exceptionsList.setVisibleRowCount(12);
        JScrollPane exceptionsScrollPane = new JScrollPane(exceptionsList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        exceptionsScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));  //puts a thin line border around outer edge of panel
        exceptionsListPanel.add(exceptionsScrollPane);
        exceptionsTab.add(exceptionsListPanel);
        //to finish, add Exceptions Tab to tab panel
        tabbedPane.add(EXCEPTIONS, exceptionsTab);
        
        
        //*****create Shift Pay Tab*****
        //this will show three panels (each with directions and a list) from left to right: shiftResultsPanel, shiftDayPanel, & shiftMethodPanel
        shiftPayTab = new JPanel();  //panel for Shift Pay tab
        shiftPayDirectionsAllListsPanel = new JPanel(); //panel to hold directions and all lists
        shiftPayDirectionsAllListsPanel.setLayout(new BoxLayout(shiftPayDirectionsAllListsPanel, BoxLayout.Y_AXIS));  //sets layout of objects in shiftPayDirectionsAllListsPanel. orders objects top to bottom
        shiftPayDirectionsAllListsPanel.add(Box.createRigidArea(new Dimension(0, 10)));  //creates space between edge of panel and directions
        shiftPayDirections.setAlignmentX(CENTER_ALIGNMENT);  //center shiftPayTab directions when they get put in the panel
        shiftPayDirectionsAllListsPanel.add(shiftPayDirections);  //add shiftPayTab directions as first object in shiftPayDirectionsAllListsPanel
        shiftPayDirectionsAllListsPanel.add(Box.createRigidArea(new Dimension(0, 10)));  //creates space between directions and next object
        
        shiftPayAllListsPanel = new JPanel();  //new panel that will hold all three lists (resultsPanel, dayPanel, and methodPanel) from left to right using FlowLayout
        
            //create resultsPanel - will hold directions, a list, and a help button
            shiftResultsPanel = new JPanel(); //first panel for shift pay
            shiftResultsPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));  //puts a thin line border around outer edge of panel
            shiftResultsPanel.setLayout(new BoxLayout(shiftResultsPanel, BoxLayout.Y_AXIS));  //sets layout of objects within shiftResultsPanel. orders objects top to bottom
            shiftResultsPanel.add(Box.createRigidArea(new Dimension(0, 5)));  //creates space between edge of panel and directions for shiftResultsPanel
            shiftResultsDirections.setAlignmentX(CENTER_ALIGNMENT);  //center directions in shiftResultsPanel
            shiftResultsPanel.add(shiftResultsDirections);  //add directions as first object in shiftResultsPanel
            shiftResultsPanel.add(Box.createRigidArea(new Dimension(0, 10)));  //creates space between directions and list
            
            resultsList = new JList<>(resultsOptions);  //create list and add resultsOptions array to it
            resultsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //only one item from list can be selected at a time
            resultsList.addListSelectionListener(new resultsSelectionHandler());  //connects ListSelectionListener to the list
            resultsList.setAlignmentX(CENTER_ALIGNMENT);  //centers list in panel
            //Must stretch the list to take up same space as other lists on the ShiftPayTab. Will create a scroll pane to do this.
            resultsList.setVisibleRowCount(7);  //sets number of visible rows in the list that will be placed in a scroll pane
            JScrollPane resultsScrollPane = new JScrollPane(resultsList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);  //creates scroll pane, sets scroll options, adds resultsList to scroll pane
            shiftResultsPanel.add(resultsScrollPane); //add scroll pane with list as second object in shiftResultsPanel
            shiftResultsPanel.add(Box.createRigidArea(new Dimension(0, 10)));  //creates space between list and help button
            
            resultsHelpButton = new JButton();  //help button that will launch separate window to explain choices in resultsList
            resultsHelpButton.setText("Help");  //sets text seen on the button
            resultsHelpButton.addActionListener(new java.awt.event.ActionListener() {  //connects ActionListener so pressing the button has a result
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    resultsHelpButtonPressed(evt);
                }
            });
            resultsHelpButton.setAlignmentX(CENTER_ALIGNMENT);  //centers button in panel
            shiftResultsPanel.add(resultsHelpButton);  //add button as third object in shiftResultsPanel
            shiftResultsPanel.add(Box.createRigidArea(new Dimension(0, 10)));  //create extra space at bottom of shiftResultsPanel.  Now shiftResultsPanel is complete.
            
        shiftPayAllListsPanel.add(shiftResultsPanel);  //add shiftResultsPanel to shiftPayAllListsPanel
        shiftPayAllListsPanel.add(Box.createRigidArea(new Dimension(8, 0)));  //create extra space horizontally between shiftResultsPanel and second list panel we'll build
        
            //create shiftDayPanel - will hold directions, a list, and a help button
            shiftDayPanel = new JPanel();  //second panel for shift pay
            shiftDayPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));  //puts a thin line border around outer edge of panel
            shiftDayPanel.setLayout(new BoxLayout(shiftDayPanel, BoxLayout.Y_AXIS));  //sets layout of objects within shiftDayPanel. orders objects top to bottom
            shiftDayPanel.add(Box.createRigidArea(new Dimension(0, 5)));  //creates space between edge of panel and directions for shiftDayPanel
            shiftDayDirections.setAlignmentX(CENTER_ALIGNMENT);  //center directions in shiftDayPanel
            shiftDayPanel.add(shiftDayDirections);  //add directions as first object in shiftDayPanel
            shiftDayPanel.add(Box.createRigidArea(new Dimension(0, 10)));  //creates space between directions and list
            
            dayList = new JList<>(dayOptions);  //create list and add dayOptions array to it
            dayList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //only one item from list can be selected at a time
            dayList.addListSelectionListener(new daySelectionHandler());  //connects ListSelectionListener to the list
            dayList.setAlignmentX(CENTER_ALIGNMENT);  //centers list in panel
            //Must stretch the list to take up same space as other lists on the ShiftPayTab. Will create a scroll pane to do this.
            dayList.setVisibleRowCount(7);  //sets number of visible rows in the list that will be placed in a scroll pane
            JScrollPane dayScrollPane = new JScrollPane(dayList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);  //creates scroll pane, sets scroll options, adds dayList to scroll pane
            shiftDayPanel.add(dayScrollPane);  //add scroll pane with list as second object in shiftDayPanel
            shiftDayPanel.add(Box.createRigidArea(new Dimension(0, 10)));  //creates space between list and help button
            
            dayHelpButton = new JButton();  //help button that will launch separate window to explain choices in dayList
            dayHelpButton.setText("Help");  //sets text seen on the button
            dayHelpButton.addActionListener(new java.awt.event.ActionListener() {  //connects ActionListener so pressing the button has a result
                 @Override
                 public void actionPerformed(java.awt.event.ActionEvent evt) {
                    dayHelpButtonPressed(evt);
                }
            });
            dayHelpButton.setAlignmentX((CENTER_ALIGNMENT));  //centers button in panel
            shiftDayPanel.add(dayHelpButton);  //add button as third object in shiftDayPanel
            shiftDayPanel.add(Box.createRigidArea(new Dimension(0, 10)));  //create extra space at bottom of shiftDayPanel.  Now shiftDayPanel is complete.
        
        shiftPayAllListsPanel.add(shiftDayPanel);  //add shiftDayPanel to shiftPayAllListsPanel
        shiftPayAllListsPanel.add(Box.createRigidArea(new Dimension(8, 0)));  //create extra space horizontally between shiftResultsPanel and third list panel we'll build
        
            //create shiftMethodPanel - will hold directions, and a list (no help button)
            shiftMethodPanel = new JPanel();  //third panel for shift pay
            shiftMethodPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));  //puts a thin line border around outer edge of panel
            shiftMethodPanel.setLayout(new BoxLayout(shiftMethodPanel, BoxLayout.Y_AXIS));  //sets layout of objects within shiftMethodPanel. orders objects top to bottom
            shiftMethodPanel.add(Box.createRigidArea(new Dimension(0, 5)));  //creates space between edge of panel and directions for shiftMethodPanel
            shiftMethodDirections.setAlignmentX(CENTER_ALIGNMENT);  //center directions in shiftMethodPanel
            shiftMethodPanel.add(shiftMethodDirections);  //add directions as first object in shiftMethodPanel
            shiftMethodPanel.add(Box.createRigidArea(new Dimension(0, 10)));  //creates space between directions and list
            
            methodList = new JList<>(methodOptions);  //create list and add methodOptions array to it. No need to make list artifically bigger as this is largest list on this tab
            methodList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //only one item from list can be selected at a time
            methodList.addListSelectionListener(new methodSelectionHandler());  //connects ListSelectionListener to the list
            methodList.setAlignmentX(CENTER_ALIGNMENT);  //centers list in panel
            shiftMethodPanel.add(methodList);  //adds list as second object in shiftMethodPanel
            shiftMethodPanel.add(Box.createRigidArea(new Dimension(0, 52)));  //creates space between list and end of panel.  Now shiftMethodPanel is complete.
            
        shiftPayAllListsPanel.add(shiftMethodPanel);  //add shiftMethodPanel to shiftPayAllListsPanel. Now shiftPayAllListsPanel is complete
        
        shiftPayDirectionsAllListsPanel.add(shiftPayAllListsPanel);  //add all-lists panel as second object to shiftPayDirectionsAllListsPanel (already holds directions). Now shiftPayDirectionsAllListsPanel is complete.

        shiftPayTab.add(shiftPayDirectionsAllListsPanel); //add shiftPayDirectionsAllListsPanel to Shift Pay Tab
        //to finish, add Shift Pay Tab to tab panel
        tabbedPane.add(SHIFT_PAY, shiftPayTab);

        
        //*****create Overtime Tab*****
        overtimeTab = new JPanel();
        overtimeTab.setLayout(new BoxLayout(overtimeTab, BoxLayout.Y_AXIS));  //orients objects top to bottom
        
        JPanel overtimeComboBoxWithDirectionsPanel = new JPanel();  //new panel to hold combo box and directions. Multiple panels make objects easier to size and center
        overtimeComboBoxWithDirectionsPanel.setLayout(new BoxLayout(overtimeComboBoxWithDirectionsPanel, BoxLayout.Y_AXIS));  //orients objects top to bottom
        overtimeComboBoxWithDirectionsPanel.add(Box.createRigidArea(new Dimension(0, 15)));  //add space between edge of panel and directions
        overtimeDirections.setAlignmentX(CENTER_ALIGNMENT);
        overtimeComboBoxWithDirectionsPanel.add(overtimeDirections);  //add directions as first object to top of overtimeComboBoxWithDirectionsPanel
        
        JPanel overtimeComboBoxPanel = new JPanel();  //new panel to only hold combo box. This keeps combo box at default size
        JComboBox overtimeComboBox = new JComboBox<>(contextOptions);  //create combo box and add items items from array as options
        overtimeComboBox.setEditable(false);  //user can't edit items in combo box
        overtimeComboBox.addItemListener(this);  //connects ItemListener to the combo box
        overtimeComboBox.setAlignmentX(CENTER_ALIGNMENT);
        overtimeComboBoxPanel.add(overtimeComboBox);  //add combo box as only object in overtimeComboBoxPanel
        
        overtimeComboBoxWithDirectionsPanel.add(overtimeComboBoxPanel); //add overtimeComboBoxPanel as second object in overtimeComboBoxWithDirectionsPanel, under directions

        overtimeTab.add(overtimeComboBoxWithDirectionsPanel);  //add panel with directions and combo box to the top of the tab
        
            //create cards that will show different lists depending on user's drop down choice
            //create weekly context card
            JPanel weeklyOvertimeCard = new JPanel();  //card
            weeklyList = new JList<>(weeklyContextOptions);  //create list and add weeklyContextOptions array to it
            weeklyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //only one item can be selected at a time
            weeklyList.addListSelectionListener(new weeklySelectionHandler());  //connect to a ListSelectionListener
            weeklyList.setVisibleRowCount(12);
            JScrollPane weeklyListScrollPane = new JScrollPane(weeklyList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            weeklyListScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));  //puts a thin line border around outer edge of panel
            weeklyOvertimeCard.add(weeklyListScrollPane);  //add list to weeklyOvertimeCard
           
            //create daily context card
            JPanel dailyOvertimeCard = new JPanel();  //card
            dailyList = new JList<>(dailyContextOptions);  //create list and add dailyContextOptions array to it
            dailyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  
            dailyList.addListSelectionListener(new dailySelectionHandler());
            dailyList.setVisibleRowCount(12);
            JScrollPane dailyListScrollPane = new JScrollPane(dailyList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            dailyListScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
            dailyOvertimeCard.add(dailyListScrollPane);
            
            //create rest context card
            JPanel restOvertimeCard = new JPanel();  //card
            restList = new JList<>(restContextOptions);  //create list and add restContextOptions array to it
            restList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            restList.addListSelectionListener(new restSelectionHandler());
            restList.setVisibleRowCount(12);
            JScrollPane restListScrollPane = new JScrollPane(restList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            restListScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
            restOvertimeCard.add(restListScrollPane);
    
            //associate each card with an item in the Overtime tab combo box
            overtimeCards = new JPanel(new CardLayout());
            overtimeCards.add(weeklyOvertimeCard, context1);
            overtimeCards.add(dailyOvertimeCard, context2);
            overtimeCards.add(restOvertimeCard, context3);
            
        overtimeTab.add(overtimeCards); //add cards to the Overtime Tab
        //to finish, add Overtime Tab to tab panel
        tabbedPane.add(OVERTIME, overtimeTab);
        
        
        //*****create Regulations Tab*****
        regulationsTab = new JPanel();  //panel for Regulations Tab
        regulationsTab.setLayout(new BoxLayout(regulationsTab, BoxLayout.Y_AXIS));  //sets layout of objects in regulationsTab. orders objects top to bottom

        JPanel regComboBoxWithDirectionsPanel = new JPanel();  //new panel to hold combo box and directions. Multiple panels makes objects easier to size and center
        regComboBoxWithDirectionsPanel.setLayout(new BoxLayout(regComboBoxWithDirectionsPanel, BoxLayout.Y_AXIS));  //give regComboBoxWithDirectionsPanel vertical layout and orders objects top to bottom
        regComboBoxWithDirectionsPanel.add(Box.createRigidArea(new Dimension(0, 15))); //creates space between edge of panel and directions. First object in regComboBoxWithDirectionsPanel
        regulationDirections.setAlignmentX(CENTER_ALIGNMENT);  //center directions when they get put into a panel
        regComboBoxWithDirectionsPanel.add(regulationDirections);  //add directions as second object in regComboBoxWithDirectionsPanel
        
        JPanel regionComboBoxPanel = new JPanel();  //new panel to only hold combo box, which means combo box will only be as large as it needs to be
        JComboBox regComboBox = new JComboBox<>(regComboBoxItems);  //create combo box and add items from array as options
        regComboBox.setEditable(false);  //user can't edit items in the combo box after program is run
        regComboBox.addItemListener(this);  //connects ItemListener class to the combo box so we can read user's input
        regComboBox.setAlignmentX(CENTER_ALIGNMENT);  //center combo box when it gets put into a panel
        regionComboBoxPanel.add(regComboBox);  //add combo box to panel as only object in regionComboBoxPanel. Now regionComboBoxPanel is complete
        
        regComboBoxWithDirectionsPanel.add(regionComboBoxPanel);  //add panel with combo box to panel, under directions, as third object in regComboBoxWithDirectionsPanel
        regulationsTab.add(regComboBoxWithDirectionsPanel);  //add panel with directions and combo box to the tab as the first object
        
            //create cards that will show different lists depending on user's drop down choice
            //create region1 (USA) card
            JPanel region1Card = new JPanel();
            region1Card.setLayout(new BoxLayout(region1Card, BoxLayout.Y_AXIS));
            region1Card.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
            JPanel reg1ComboBoxDirectionsPanel = new JPanel(); //panel for states/territory combo box and directions
            reg1ComboBoxDirectionsPanel.setLayout(new BoxLayout(reg1ComboBoxDirectionsPanel, BoxLayout.Y_AXIS));
            JPanel reg1ComboBoxPanel = new JPanel();
            region1Directions.setAlignmentX(CENTER_ALIGNMENT);
            reg1ComboBoxDirectionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            reg1ComboBoxDirectionsPanel.add(region1Directions);
            JComboBox reg1ChoiceComboBox = new JComboBox<>(region1ComboBoxItems); 
            reg1ChoiceComboBox.setEditable(false);
            reg1ChoiceComboBox.addItemListener(this);
            reg1ChoiceComboBox.setAlignmentX(CENTER_ALIGNMENT);
            reg1ComboBoxPanel.add(reg1ChoiceComboBox);
            reg1ComboBoxDirectionsPanel.add(reg1ComboBoxPanel);
            region1Card.add(reg1ComboBoxDirectionsPanel);
            
                //create Federal card within USA card
                JPanel region1FederalCard = new JPanel();
                region1FederalList = new JList<>(region1FederalOptions);
                region1FederalList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                region1FederalList.addListSelectionListener(new region1FederalListSelectionHandler());
                region1FederalList.setVisibleRowCount(9);
                JScrollPane region1FederalScrollPane = new JScrollPane(region1FederalList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                region1FederalScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                region1FederalCard.add(region1FederalScrollPane);
                
                //create AK card within USA card
                JPanel region1AKCard = new JPanel();
                region1AKList = new JList<>(region1AKOptions);
                region1AKList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                region1AKList.addListSelectionListener(new region1AKListSelectionHandler());
                region1AKList.setVisibleRowCount(9);
                JScrollPane region1AKScrollPane = new JScrollPane(region1AKList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                region1AKScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                region1AKCard.add(region1AKScrollPane);
                
                //create CA card within USA card
                JPanel region1CACard = new JPanel();
                region1CAList = new JList<>(region1CAOptions);
                region1CAList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                region1CAList.addListSelectionListener(new region1CAListSelectionHandler());
                region1CAList.setVisibleRowCount(9);
                JScrollPane region1CAScrollPane = new JScrollPane(region1CAList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                region1CAScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                region1CACard.add(region1CAScrollPane);
                
                //create CO card within USA card
                JPanel region1COCard = new JPanel();
                region1COList = new JList<>(region1COOptions);
                region1COList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                region1COList.addListSelectionListener(new region1COListSelectionHandler());
                region1COList.setVisibleRowCount(9);
                JScrollPane region1COScrollPane = new JScrollPane(region1COList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                region1COScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                region1COCard.add(region1COScrollPane);
                
                //create CT card within USA card
                JPanel region1CTCard = new JPanel();
                region1CTList = new JList<>(region1CTOptions);
                region1CTList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                region1CTList.addListSelectionListener(new region1CTListSelectionHandler());
                region1CTList.setVisibleRowCount(9);
                JScrollPane region1CTScrollPane = new JScrollPane(region1CTList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                region1CTScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                region1CTCard.add(region1CTScrollPane);               
                
                //create KA card within USA card
                JPanel region1KSCard = new JPanel();
                region1KSList = new JList<>(region1KSOptions);
                region1KSList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                region1KSList.addListSelectionListener(new region1KSListSelectionHandler());
                region1KSList.setVisibleRowCount(9);
                JScrollPane region1KSScrollPane = new JScrollPane(region1KSList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                region1KSScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                region1KSCard.add(region1KSScrollPane);
                
                //create KY card within USA card
                JPanel region1KYCard = new JPanel();
                region1KYList = new JList<>(region1KYOptions);
                region1KYList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                region1KYList.addListSelectionListener(new region1KYListSelectionHandler());
                region1KYList.setVisibleRowCount(9);
                JScrollPane region1KYScrollPane = new JScrollPane(region1KYList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                region1KYScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                region1KYCard.add(region1KYScrollPane);
                
                //create MA card within USA card
                JPanel region1MACard = new JPanel();
                region1MAList = new JList<>(region1MAOptions);
                region1MAList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                region1MAList.addListSelectionListener(new region1MAListSelectionHandler());
                region1MAList.setVisibleRowCount(9);
                JScrollPane region1MAScrollPane = new JScrollPane(region1MAList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                region1MAScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                region1MACard.add(region1MAScrollPane);
                
                //create MN card within USA card
                JPanel region1MNCard = new JPanel();
                region1MNList = new JList<>(region1MAOptions);
                region1MNList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                region1MNList.addListSelectionListener(new region1MNListSelectionHandler());
                region1MNList.setVisibleRowCount(9);
                JScrollPane region1MNScrollPane = new JScrollPane(region1MNList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                region1MNScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                region1MNCard.add(region1MNScrollPane);
                
                //create NV card within USA card
                JPanel region1NVCard = new JPanel();
                region1NVList = new JList<>(region1NVOptions);
                region1NVList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                region1NVList.addListSelectionListener(new region1NVListSelectionHandler());
                region1NVList.setVisibleRowCount(9);
                JScrollPane region1NVScrollPane = new JScrollPane(region1NVList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                region1NVScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                region1NVCard.add(region1NVScrollPane);
                
                //create NJ card within USA card
                JPanel region1NJCard = new JPanel();
                region1NJList = new JList<>(region1NJOptions);
                region1NJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                region1NJList.addListSelectionListener(new region1NJListSelectionHandler());
                region1NJList.setVisibleRowCount(9);
                JScrollPane region1NJScrollPane = new JScrollPane(region1NJList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                region1NJScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                region1NJCard.add(region1NJScrollPane);
                
                //create NY card within USA card
                JPanel region1NYCard = new JPanel();
                region1NYList = new JList<>(region1NYOptions);
                region1NYList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                region1NYList.addListSelectionListener(new region1NYListSelectionHandler());
                region1NYList.setVisibleRowCount(9);
                JScrollPane region1NYScrollPane = new JScrollPane(region1NYList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                region1NYScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                region1NYCard.add(region1NYScrollPane);
                
                //create OR card within USA card
                JPanel region1ORCard = new JPanel();
                region1ORList = new JList<>(region1OROptions);
                region1ORList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                region1ORList.addListSelectionListener(new region1ORListSelectionHandler());
                region1ORList.setVisibleRowCount(9);
                JScrollPane region1ORScrollPane = new JScrollPane(region1ORList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                region1ORScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                region1ORCard.add(region1ORScrollPane);
                
                //create PA card within USA card
                JPanel region1PACard = new JPanel();
                region1PAList = new JList<>(region1PAOptions);
                region1PAList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                region1PAList.addListSelectionListener(new region1PAListSelectionHandler());
                region1PAList.setVisibleRowCount(9);
                JScrollPane region1PAScrollPane = new JScrollPane(region1PAList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                region1PAScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                region1PACard.add(region1PAScrollPane);
                
                //create PR card within USA card
                JPanel region1PRCard = new JPanel();
                region1PRList = new JList<>(region1PROptions);
                region1PRList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                region1PRList.addListSelectionListener(new region1PRListSelectionHandler());
                region1PRList.setVisibleRowCount(9);
                JScrollPane region1PRScrollPane = new JScrollPane(region1PRList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                region1PRScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                region1PRCard.add(region1PRScrollPane);
                
                //create RI card within USA card
                JPanel region1RICard = new JPanel();
                region1RIList = new JList<>(region1RIOptions);
                region1RIList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                region1RIList.addListSelectionListener(new region1RIListSelectionHandler());
                region1RIList.setVisibleRowCount(9);
                JScrollPane region1RIScrollPane = new JScrollPane(region1RIList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                region1RIScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                region1RICard.add(region1RIScrollPane); 
                
                //create WA card within USA card
                JPanel region1WACard = new JPanel();
                region1WAList = new JList<>(region1WAOptions);
                region1WAList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                region1WAList.addListSelectionListener(new region1WAListSelectionHandler());
                region1WAList.setVisibleRowCount(9);
                JScrollPane region1WAScrollPane = new JScrollPane(region1WAList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                region1WAScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                region1WACard.add(region1WAScrollPane);
                
                //create DC card within USA card
                JPanel region1DCCard = new JPanel();
                region1DCList = new JList<>(region1DCOptions);
                region1DCList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                region1DCList.addListSelectionListener(new region1DCListSelectionHandler());
                region1DCList.setVisibleRowCount(9);
                JScrollPane region1DCScrollPane = new JScrollPane(region1DCList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                region1DCScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                region1DCCard.add(region1DCScrollPane);    
                
                //create WI card within USA card
                JPanel region1WICard = new JPanel();
                region1WIList = new JList<>(region1WIOptions);
                region1WIList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                region1WIList.addListSelectionListener(new region1WIListSelectionHandler());
                region1WIList.setVisibleRowCount(9);
                JScrollPane region1WIScrollPane = new JScrollPane(region1WIList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                region1WIScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                region1WICard.add(region1WIScrollPane);  
                
                //associate each card with an item in the USA combo box
                region1Cards = new JPanel(new CardLayout());
                region1Cards.add(region1FederalCard, region1Option1);
                region1Cards.add(region1AKCard, region1Option2);
                region1Cards.add(region1CACard, region1Option3);
                region1Cards.add(region1COCard, region1Option4);
                region1Cards.add(region1CTCard, region1Option5);
                region1Cards.add(region1KSCard, region1Option6);
                region1Cards.add(region1KYCard, region1Option7);
                region1Cards.add(region1MACard, region1Option8);
                region1Cards.add(region1MNCard, region1Option9);
                region1Cards.add(region1NVCard, region1Option10);
                region1Cards.add(region1NJCard, region1Option11);
                region1Cards.add(region1NYCard, region1Option12);
                region1Cards.add(region1ORCard, region1Option13);
                region1Cards.add(region1PACard, region1Option14);
                region1Cards.add(region1PRCard, region1Option15);
                region1Cards.add(region1RICard, region1Option16);
                region1Cards.add(region1WACard, region1Option17);
                region1Cards.add(region1DCCard, region1Option18);
                region1Cards.add(region1WICard, region1Option19);

            region1Card.add(region1Cards);  //add all state cards to the US card
        
            //create region2 (Canada) card
            JPanel region2Card = new JPanel();
            region2Card.setLayout(new BoxLayout(region2Card, BoxLayout.Y_AXIS));
            region2Card.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
            JPanel reg2ComboBoxDirectionsPanel = new JPanel();  //panel for directions and provinces/territories combo box
            reg2ComboBoxDirectionsPanel.setLayout(new BoxLayout(reg2ComboBoxDirectionsPanel, BoxLayout.Y_AXIS));
            JPanel reg2ComboBoxPanel = new JPanel();
            region2Directions.setAlignmentX(CENTER_ALIGNMENT);
            reg2ComboBoxDirectionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            reg2ComboBoxDirectionsPanel.add(region2Directions);
            JComboBox reg2ChoiceComboBox = new JComboBox<>(region2ComboBoxItems);   
            reg2ChoiceComboBox.setEditable(false);
            reg2ChoiceComboBox.addActionListener(this);
            reg2ChoiceComboBox.setAlignmentX(CENTER_ALIGNMENT);
            reg2ComboBoxPanel.add(reg2ChoiceComboBox);
            reg2ComboBoxDirectionsPanel.add(reg2ComboBoxPanel);
            region2Card.add(reg2ComboBoxDirectionsPanel);
            
            JPanel region2ListPanel = new JPanel();
            region2List = new JList<>(region2CardOptions);
            region2List.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            region2List.addListSelectionListener(new region2SelectionHandler());
            region2List.setVisibleRowCount(9);  //sets the number of rows to show before the list has to scroll
            JScrollPane region2ScrollPane = new JScrollPane(region2List, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            region2ScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
            region2ListPanel.add(region2ScrollPane);
            region2Card.add(region2ListPanel);

            //create region3 (UK/EU) card
            JPanel region3Card = new JPanel();
            region3Card.setLayout(new BoxLayout(region3Card, BoxLayout.Y_AXIS));
            region3Card.add(Box.createRigidArea(new Dimension(0, 10)));
            region3Directions.setAlignmentX(CENTER_ALIGNMENT);
            region3Card.add(region3Directions);
            region3Card.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
            
            JPanel region3ListPanel = new JPanel();
            region3List = new JList<>(region3CardOptions);
            region3List.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            region3List.addListSelectionListener(new region3SelectionHandler());
            region3List.setVisibleRowCount(11); //sets the number of rows to show before the list has to scroll
            JScrollPane region3ScrollPane = new JScrollPane(region3List, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            region3ScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
            region3ListPanel.add(region3ScrollPane);
            region3Card.add(region3ListPanel);
            
            //associate each region card with an item in the region combo box
            regionCards = new JPanel(new CardLayout());
            regionCards.add(region1Card, region1);
            regionCards.add(region2Card, region2);
            regionCards.add(region3Card, region3);
        
        regulationsTab.add(regionCards);  //add US, Canada, and UK/EU cards to the region card
        
        //to finish, add Regulations Tab to tab panel
        tabbedPane.add(REGULATIONS, regulationsTab);
        
        //build buttom panel
        JPanel recBox = new JPanel();
        recBox.setLayout(new BoxLayout(recBox, BoxLayout.Y_AXIS));
        JPanel recLabelPanel = new JPanel();
        recBox.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 1));
        recLabel = new JLabel();
        recLabel.setText("               We recommend you use:                ");
        recLabel.setAlignmentX(CENTER_ALIGNMENT);
        recLabelPanel.add(recLabel);
        recBox.add(recLabelPanel);
        recOutputLabel = new JLabel();
        recOutputLabel.setAlignmentX(CENTER_ALIGNMENT);
        recOutputLabel.setFont(new java.awt.Font("Tahoma", 1, 11));
        recOutputLabel.setText(recOutputPlaceholder);
        recBox.add(recOutputLabel);
        recBox.add(Box.createRigidArea(new Dimension(0,4)));
        
        //put main tool's frame together
        pane.add(tabbedPane); //tabbed panes that switch based on the tab selected
        pane.add(recBox); //bottom pane with recommended template output
    }
    
    @Override
    public void itemStateChanged(ItemEvent evt) {  //switches the cards based on user's drop-down choice
        CardLayout acl = (CardLayout)(accrualCards.getLayout());  //switches cards on Accruals Tab
        acl.show(accrualCards, (String)evt.getItem());
        
        CardLayout ocl = (CardLayout)(overtimeCards.getLayout());  //switches cards on Overtime Tab
        ocl.show(overtimeCards, (String)evt.getItem());
        
        CardLayout rcl = (CardLayout) (regionCards.getLayout());  //switches region cards and shows correct dropdown of available states/provinces on Regulations Tab
        rcl.show(regionCards, (String)evt.getItem());
        
        CardLayout r1cl = (CardLayout) (region1Cards.getLayout());  //switches state/territory cards and shows correct list of available templates for each after choosing region1 (US) on Regulations Tab
        r1cl.show(region1Cards, (String)evt.getItem()); 
    }
    
    public void resultsHelpButtonPressed(java.awt.event.ActionEvent evt) {  //when left Help button is pressed, launches picture showing examples of options in first list of Shift Pay Tab
        JFrame helpFrame1 = new JFrame();
        helpFrame1.setUndecorated(false);
        ImageIcon image = new ImageIcon(getClass().getResource("/images/One.jpg"));
        JLabel lbl = new JLabel(image);
        helpFrame1.getContentPane().add(lbl);
        helpFrame1.setVisible(true);
        helpFrame1.setSize(image.getIconWidth(), image.getIconHeight()+ 50 );
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    }
    
        public void dayHelpButtonPressed(java.awt.event.ActionEvent evt) {  //when right Help button is pressed, launches picture showing examples of options in first list of Shift Pay Tab
        JFrame helpFrame2 = new JFrame();
        helpFrame2.setUndecorated(false);
        ImageIcon image = new ImageIcon(getClass().getResource("/images/Two.jpg"));
        JLabel lbl = new JLabel(image);
        helpFrame2.getContentPane().add(lbl);
        helpFrame2.setVisible(true);
        helpFrame2.setSize(image.getIconWidth(), image.getIconHeight()+ 50 );
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    }
    
    //all classes below that implement ListSelectionListener are used to set the template recommended based on the user's input in a list
    class withAccrualsSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent le) {
            if(!isChanging) {  //clears choices made in other lists, prior to selection made in this list. Prevents lists with one choice where choice has already been made from being un-selectable
                isChanging = true;  
                withoutAccrualsList.clearSelection();
                miscAccrualsList.clearSelection();
                exceptionsList.clearSelection();
                resultsList.clearSelection();
                dayList.clearSelection();
                methodList.clearSelection();
                weeklyList.clearSelection();
                dailyList.clearSelection();
                restList.clearSelection();
                region1FederalList.clearSelection();
                region1AKList.clearSelection();
                region1CAList.clearSelection();
                region1COList.clearSelection();
                region1CTList.clearSelection();
                region1KSList.clearSelection();
                region1KYList.clearSelection();
                region1MAList.clearSelection();
                region1MNList.clearSelection();
                region1NVList.clearSelection();
                region1NJList.clearSelection();
                region1NYList.clearSelection();
                region1ORList.clearSelection();
                region1PAList.clearSelection();
                region1PRList.clearSelection();
                region1RIList.clearSelection();
                region1WAList.clearSelection();
                region1DCList.clearSelection();
                region1WIList.clearSelection();
                region2List.clearSelection();
                region3List.clearSelection();
                isChanging = false;
            }
            listIndex = withAccrualsList.getSelectedIndex();
            if(listIndex != -1) { //-1 means all were selected, which is impossible with single_selection, so this basically becomes if anything was selected
                switch (listIndex) {
                    case 0:
                        recommendOutput = "ATTENDANCE_POINTS";
                        break;
                    case 2:   //indices 1 & 3 are line breaks
                        recommendOutput = "OT_TO_TOIL";
                        break;
                    case 4:
                        recommendOutput = "BANK_ANNUAL_CARRY";
                        break;
                    case 5:
                        recommendOutput = "BANK_ANNUAL_W_CARRY_BANK";
                        break;
                    case 6:
                        recommendOutput = "BANK_MONTHLY_CARRY";
                        break;
                    case 7:
                        recommendOutput = "BANK_SEMI_MONTHLY_BEG_CARRY";
                        break;
                    case 8:
                        recommendOutput = "BANK_SEMI_MONTHLY_END_CARRY";
                        break;
                    case 9:
                        recommendOutput = "BANK_BIWEEKLY_BEG_CARRY";
                        break;
                    case 10:
                        recommendOutput = "BANK_BIWEEKLY_END_CARRY";
                        break;
                    case 11:
                        recommendOutput = "BANK_WEEKLY_BEG_CARRY";
                        break;
                    case 12:
                        recommendOutput = "BANK_WEEKLY_END_CARRY";
                        break;
                    default:
                        recommendOutput = recOutputPlaceholder;
                        break;
                }
            }
            recOutputLabel.setText(recommendOutput);
        }
    }
    
    class withoutAccrualsSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent le) {
            if(!isChanging) {  //clears choices made in other lists, prior to selection made in this list. Prevents lists with one choice where choice has already been made from being un-selectable
                isChanging = true;  
                withAccrualsList.clearSelection();
                miscAccrualsList.clearSelection();
                exceptionsList.clearSelection();
                resultsList.clearSelection();
                dayList.clearSelection();
                methodList.clearSelection();
                weeklyList.clearSelection();
                dailyList.clearSelection();
                restList.clearSelection();
                region1FederalList.clearSelection();
                region1AKList.clearSelection();
                region1CAList.clearSelection();
                region1COList.clearSelection();
                region1CTList.clearSelection();
                region1KSList.clearSelection();
                region1KYList.clearSelection();
                region1MAList.clearSelection();
                region1MNList.clearSelection();
                region1NVList.clearSelection();
                region1NJList.clearSelection();
                region1NYList.clearSelection();
                region1ORList.clearSelection();
                region1PAList.clearSelection();
                region1PRList.clearSelection();
                region1RIList.clearSelection();
                region1WAList.clearSelection();
                region1DCList.clearSelection();
                region1WIList.clearSelection();
                region2List.clearSelection();
                region3List.clearSelection();
                isChanging = false;
            }
            listIndex = withoutAccrualsList.getSelectedIndex();
            if(listIndex != -1) {  //-1 means all were selected, which is impossible with single_selection, so this basically becomes if anything was selected
                switch (listIndex) {
                    case 0:
                        recommendOutput = "BANK  or  BANK_MA";
                        break;
                    case 1:
                        recommendOutput = "BANK_CARRYOVER";
                        break;
                    default:
                        recommendOutput = recOutputPlaceholder;
                        break;
                }
            }
            recOutputLabel.setText(recommendOutput);
        }
    }
    
    class miscAccrualsSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent le) {
            if(!isChanging) {  //clears choices made in other lists, prior to selection made in this list. Prevents lists with one choice where choice has already been made from being un-selectable
                isChanging = true;  
                withAccrualsList.clearSelection();
                withoutAccrualsList.clearSelection();
                exceptionsList.clearSelection();
                resultsList.clearSelection();
                dayList.clearSelection();
                methodList.clearSelection();
                weeklyList.clearSelection();
                dailyList.clearSelection();
                restList.clearSelection();
                region1FederalList.clearSelection();
                region1AKList.clearSelection();
                region1CAList.clearSelection();
                region1COList.clearSelection();
                region1CTList.clearSelection();
                region1KSList.clearSelection();
                region1KYList.clearSelection();
                region1MAList.clearSelection();
                region1MNList.clearSelection();
                region1NVList.clearSelection();
                region1NJList.clearSelection();
                region1NYList.clearSelection();
                region1ORList.clearSelection();
                region1PAList.clearSelection();
                region1PRList.clearSelection();
                region1RIList.clearSelection();
                region1WAList.clearSelection();
                region1DCList.clearSelection();
                region1WIList.clearSelection();
                region2List.clearSelection();
                region3List.clearSelection();
                isChanging = false;
            }    
            listIndex = miscAccrualsList.getSelectedIndex();
            if(listIndex != -1) {  //-1 means all were selected, which is impossible with single_selection, so this basically becomes if anything was selected
                switch (listIndex) {
                    case 0: 
                        recommendOutput = "BANK_ROLLING_CLEAR";
                        break;
                    case 1:
                        recommendOutput = "BANK_ROLLING_USAGE";
                        break;
                    case 2:
                        recommendOutput = "BANK_NEG_BAL_ACCRUAL_LEFT";
                        break;
                    case 3:
                        recommendOutput = "MAX_ABSENCE_LENGTH_SCHEDULE";
                        break;
                    case 4:
                        recommendOutput = "MAX_ABSENCE_LENGTH_WORKWEEK";
                        break;
                    default:
                        recommendOutput = recOutputPlaceholder;
                        break;
                }
            }
            recOutputLabel.setText(recommendOutput);
        }
    }
    
    class exceptionsSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent le) {
            if(!isChanging) {  //clears choices made in other lists, prior to selection made in this list. Prevents lists with one choice where choice has already been made from being un-selectable
                isChanging = true;        
                withAccrualsList.clearSelection();
                withoutAccrualsList.clearSelection();
                miscAccrualsList.clearSelection();
                resultsList.clearSelection();
                dayList.clearSelection();
                methodList.clearSelection();
                weeklyList.clearSelection();
                dailyList.clearSelection();
                restList.clearSelection();
                region1FederalList.clearSelection();
                region1AKList.clearSelection();
                region1CAList.clearSelection();
                region1COList.clearSelection();
                region1CTList.clearSelection();
                region1KSList.clearSelection();
                region1KYList.clearSelection();
                region1MAList.clearSelection();
                region1MNList.clearSelection();
                region1NVList.clearSelection();
                region1NJList.clearSelection();
                region1NYList.clearSelection();
                region1ORList.clearSelection();
                region1PAList.clearSelection();
                region1PRList.clearSelection();
                region1RIList.clearSelection();
                region1WAList.clearSelection();
                region1DCList.clearSelection();
                region1WIList.clearSelection();
                region2List.clearSelection();
                region3List.clearSelection();
                isChanging = false;
            }
            listIndex = exceptionsList.getSelectedIndex();
            if(listIndex != -1) {  //-1 means all were selected, which is impossible with single_selection, so this basically becomes if anything was selected
                switch (listIndex) {
                    case 0: 
                        recommendOutput = "SINGLE_EXCEPTION";
                        break;
                    case 2:  //index 1 is a line break
                        recommendOutput = "EXCEPTIONS";
                        break;
                    case 3:
                        recommendOutput = "MAX_ABSENCE_LENGTH_SCHEDULE";
                        break;
                    case 4:
                        recommendOutput = "MAX_ABSENCE_LENGTH_WORKWEEK";
                        break;
                    case 5:
                        recommendOutput = "EXCEPTIONS";
                        break;
                    case 7:  //index 6 is a line break
                        recommendOutput = "EXCEPTIONS";
                        break;
                    case 8:
                        recommendOutput = "EXCEPTIONS";
                        break;
                    case 10:  //index 9 is a line break
                        recommendOutput = "EXCEPTIONS";
                        break;
                    case 11:
                        recommendOutput = "EXCEPTIONS";
                        break;
                    case 12:
                        recommendOutput = "EXCEPTIONS";
                        break;
                    case 13:
                        recommendOutput = "EXCEPTIONS";
                        break;
                    case 14:
                        recommendOutput = "EXCEPTIONS";
                        break;
                    case 15:
                        recommendOutput = "EXCEPTIONS";
                        break;
                    case 16:
                        recommendOutput = "EXCEPTIONS";
                        break;
                    case 18:  //index 17 is a line break
                        recommendOutput = "EXCEPTIONS";
                        break;
                    case 19:
                        recommendOutput = "ANNUAL_PAYCODE_USE";
                        break;
                    case 21:  //index 20 is a line break
                        recommendOutput = "EMP_IN_OUT_PUNCH_OUTSIDE_GEOFENCE";
                        break;
                    case 22:
                        recommendOutput = "EMP_IN_OUT_PUNCH_OUTSIDE_RADIUS";
                        break;
                    case 24:  //index 23 is a line break
                        recommendOutput = "BANK_NEG_BAL_ACCRUAL_LEFT";
                        break;
                    case 26:  //index 25 is a line break
                        recommendOutput = "TOR_ONLY_EXCEPTIONS";
                        break;
                    default:
                        recommendOutput = recOutputPlaceholder;
                        break;
                }
            }
            recOutputLabel.setText(recommendOutput);
        }
    }
    
    class resultsSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent le) {
            if(!isChanging) {  //clears choices made in other lists, prior to selection made in this list. Prevents lists with one choice where choice has already been made from being un-selectable
                isChanging = true;        
                withAccrualsList.clearSelection();
                withoutAccrualsList.clearSelection();
                miscAccrualsList.clearSelection();
                exceptionsList.clearSelection();
                weeklyList.clearSelection();
                dailyList.clearSelection();
                restList.clearSelection();
                region1FederalList.clearSelection();    
                region1AKList.clearSelection();
                region1CAList.clearSelection();
                region1COList.clearSelection();
                region1CTList.clearSelection();
                region1KSList.clearSelection();
                region1KYList.clearSelection();
                region1MAList.clearSelection();
                region1MNList.clearSelection();
                region1NVList.clearSelection();
                region1NJList.clearSelection();
                region1NYList.clearSelection();
                region1ORList.clearSelection();
                region1PAList.clearSelection();
                region1PRList.clearSelection();
                region1RIList.clearSelection();
                region1WAList.clearSelection();
                region1DCList.clearSelection();
                region1WIList.clearSelection();
                region2List.clearSelection();
                region3List.clearSelection();
                isChanging = false;
            }
            listIndex = resultsList.getSelectedIndex();
            if(listIndex != 1) {  //only show second list if second item (index 1) is chosen in the first list
                dayList.setVisible(false);
            }else {
                dayList.setVisible(true);
            }
            if(listIndex != -1) {  //-1 means all were selected, which is impossible with single_selection, so this basically becomes if anything was selected
                switch (listIndex) {
                    case 0: 
                        shiftOutput1 = "PREM_";
                        break;
                    case 1:
                        shiftOutput1 = "DAY/TCP_";
                        dayList.clearSelection();
                        break;
                    case 2:
                        shiftOutput1 = "TS_CALC_";
                        break;
                    default:
                        shiftOutput1 = recOutputPlaceholder;
                        break;
                }
            } 
                if(shiftOutput1.equals("TCP_") && shiftOutput2.equals("ON_OVERLAP")) {
                    recommendOutput = "SHIFT_DIFF_TCP_ON_OVERLAP  or  SHIFT_DIFF_WEEK_TCP_ON_OVERLAP";
                } if(shiftOutput1.equals("DAY_") && shiftOutput2.equals("ON_OVERLAP")) {
                    recommendOutput = "SHIFT_DIFF_DAY_ON_OVERLAP  or  SHIFT_DIFF_WEEK_ON_OVERLAP";
                } if(shiftOutput1.equals("PREM_") && shiftOutput2.equals("ON_OVERLAP")) {
                    recommendOutput = "SHIFT_DIFF_PREM_ON_OVERLAP  or  SHIFT_DIFF_WEEK_PREM_ON_OVERLAP";
                } if(shiftOutput1.equals("TS_CALC_") && shiftOutput2.equals("ON_OVERLAP")) {
                    recommendOutput = "SHIFT_DIFF_TS_CALC_ON_OVERLAP  or  SHIFT_DIFF_WEEK_TS_CALC_ON_OVERLAP";
                } else recommendOutput = "SHIFT_DIFF_" + shiftOutput1 + shiftOutput2;  //code repeats in each listener in case user only changes selection from one list  
            recOutputLabel.setText(recommendOutput);
        }
    }
    
    class daySelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent le) {
            if(!isChanging) {  //clears choices made in other lists, prior to selection made in this list. Prevents lists with one choice where choice has already been made from being un-selectable
                isChanging = true;        
                withAccrualsList.clearSelection();
                withoutAccrualsList.clearSelection();
                miscAccrualsList.clearSelection();
                exceptionsList.clearSelection();
                weeklyList.clearSelection();
                dailyList.clearSelection();
                restList.clearSelection();
                region1FederalList.clearSelection();            
                region1AKList.clearSelection();
                region1CAList.clearSelection();
                region1COList.clearSelection();
                region1CTList.clearSelection();
                region1KSList.clearSelection();
                region1KYList.clearSelection();
                region1MAList.clearSelection();
                region1MNList.clearSelection();
                region1NVList.clearSelection();
                region1NJList.clearSelection();
                region1NYList.clearSelection();
                region1ORList.clearSelection();
                region1PAList.clearSelection();
                region1PRList.clearSelection();
                region1RIList.clearSelection();
                region1WAList.clearSelection();
                region1DCList.clearSelection();
                region1WIList.clearSelection();
                region2List.clearSelection();
                region3List.clearSelection();
                isChanging = false;
            }    
            listIndex = dayList.getSelectedIndex();
            if(listIndex != -1) {  //-1 means all were selected, which is impossible with single_selection, so this basically becomes if anything was selected
                switch (listIndex) {
                    case 0:
                        shiftOutput1 = "DAY_";
                        break;
                    case 1:
                        shiftOutput1 = "TCP_";
                        break;
                    default:
                        shiftOutput1 = recOutputPlaceholder;
                        break;
                }
            } 
            if(shiftOutput1.equals("PREM_") && shiftOutput2.equals("ON_OVERLAP")) {
                recommendOutput = "SHIFT_DIFF_PREM_ON_OVERLAP (window can span < 24 hours and span over one midnight)   or   SHIFT_DIFF_WEEK_ON_OVERLAP (window can span over multiple midnights)";
            } else if(shiftOutput1.equals("DAY_") && shiftOutput2.equals("ON_OVERLAP")) {    //code repeats in each listener in case user only changes selection from one list 
                recommendOutput = "SHIFT_DIFF_DAY_ON_OVERLAP (window can span < 24 hours and span over one midnight)   or   SHIFT_DIFF_WEEK_DAY_ON_OVERLAP (window can span over multiple midnights)";
            } else if(shiftOutput1.equals("TCP_") && shiftOutput2.equals("ON_OVERLAP")) {
                recommendOutput = "SHIFT_DIFF_TCP_ON_OVERLAP (window can span < 24 hours and span over one midnight)   or   SHIFT_DIFF_WEEK_TCP_ON_OVERLAP (window can span over multiple midnights)";
            } else if(shiftOutput1.equals("TS_CALC_") && shiftOutput2.equals("ON_OVERLAP")) {
                recommendOutput = "SHIFT_DIFF_TS_CALC_ON_OVERLAP (window can span < 24 hours and span over one midnight)   or   SHIFT_DIFF_WEEK_TS_CALC_ON_OVERLAP (window can span over multiple midnights)";
            } else recommendOutput = "SHIFT_DIFF_" + shiftOutput1 + shiftOutput2; 
            recOutputLabel.setText(recommendOutput);
        }
    }
    
    class methodSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent le) {
            if(!isChanging) {  //clears choices made in other lists, prior to selection made in this list. Prevents lists with one choice where choice has already been made from being un-selectable
                isChanging = true;        
                withAccrualsList.clearSelection();
                withoutAccrualsList.clearSelection();
                miscAccrualsList.clearSelection();
                exceptionsList.clearSelection();
                weeklyList.clearSelection();
                dailyList.clearSelection();
                restList.clearSelection();
                region1FederalList.clearSelection();                
                region1AKList.clearSelection();
                region1CAList.clearSelection();
                region1COList.clearSelection();
                region1CTList.clearSelection();
                region1KSList.clearSelection();
                region1KYList.clearSelection();
                region1MAList.clearSelection();
                region1MNList.clearSelection();
                region1NVList.clearSelection();
                region1NJList.clearSelection();
                region1NYList.clearSelection();
                region1ORList.clearSelection();
                region1PAList.clearSelection();
                region1PRList.clearSelection();
                region1RIList.clearSelection();
                region1WAList.clearSelection();
                region1DCList.clearSelection();
                region1WIList.clearSelection();
                region2List.clearSelection();
                region3List.clearSelection();
                isChanging = false;
            }
            listIndex = methodList.getSelectedIndex();
            if(listIndex != -1) {  //-1 means all were selected, which is impossible with single_selection, so this basically becomes if anything was selected
                switch (listIndex) {
                    case 0:
                        shiftOutput2 = "START";
                        break;
                    case 1:
                        shiftOutput2 = "END";
                        break;
                    case 2:
                        shiftOutput2 = "OUTSIDE_SCHEDULE";
                        break;
                    case 3:
                        shiftOutput2 = "OVERLAP_WINDOW";
                        break;
                    case 4:
                        shiftOutput2 = "ON_OVERLAP";
                        break;
                    case 5:
                        shiftOutput2 = "EMP";
                        break;
                    case 6:
                        shiftOutput2 = "LD";
                        break;
                    default:
                        shiftOutput2 = recOutputPlaceholder;
                        break;
                }   
            } 
            if(shiftOutput1.equals("PREM_") && shiftOutput2.equals("ON_OVERLAP")) {
                recommendOutput = "SHIFT_DIFF_PREM_ON_OVERLAP (window can span < 24 hours and span over one midnight)   or   SHIFT_DIFF_WEEK_ON_OVERLAP (window can span over multiple midnights)";
            } else if(shiftOutput1.equals("DAY_") && shiftOutput2.equals("ON_OVERLAP")) {    //code repeats in each listener in case user only changes selection from one list 
                recommendOutput = "SHIFT_DIFF_DAY_ON_OVERLAP (window can span < 24 hours and span over one midnight)   or   SHIFT_DIFF_WEEK_DAY_ON_OVERLAP (window can span over multiple midnights)";
            } else if(shiftOutput1.equals("TCP_") && shiftOutput2.equals("ON_OVERLAP")) {
                recommendOutput = "SHIFT_DIFF_TCP_ON_OVERLAP (window can span < 24 hours and span over one midnight)   or   SHIFT_DIFF_WEEK_TCP_ON_OVERLAP (window can span over multiple midnights)";
            } else if(shiftOutput1.equals("TS_CALC_") && shiftOutput2.equals("ON_OVERLAP")) {
                recommendOutput = "SHIFT_DIFF_TS_CALC_ON_OVERLAP (window can span < 24 hours and span over one midnight)   or   SHIFT_DIFF_WEEK_TS_CALC_ON_OVERLAP (window can span over multiple midnights)";
            } else recommendOutput = "SHIFT_DIFF_" + shiftOutput1 + shiftOutput2; 
            recOutputLabel.setText(recommendOutput);
        }
    }
    
    class weeklySelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent le) {
            if(!isChanging) {  //clears choices made in other lists, prior to selection made in this list. Prevents lists with one choice where choice has already been made from being un-selectable
                isChanging = true;        
                withAccrualsList.clearSelection();
                withoutAccrualsList.clearSelection();
                miscAccrualsList.clearSelection();
                exceptionsList.clearSelection();
                resultsList.clearSelection();
                dayList.clearSelection();
                methodList.clearSelection();
                dailyList.clearSelection();
                restList.clearSelection();
                region1FederalList.clearSelection();                
                region1AKList.clearSelection();
                region1CAList.clearSelection();
                region1COList.clearSelection();
                region1CTList.clearSelection();
                region1KSList.clearSelection();
                region1KYList.clearSelection();
                region1MAList.clearSelection();
                region1MNList.clearSelection();
                region1NVList.clearSelection();
                region1NJList.clearSelection();
                region1NYList.clearSelection();
                region1ORList.clearSelection();
                region1PAList.clearSelection();
                region1PRList.clearSelection();
                region1RIList.clearSelection();
                region1WAList.clearSelection();
                region1DCList.clearSelection();
                region1WIList.clearSelection();
                region2List.clearSelection();
                region3List.clearSelection();
                isChanging = false;
            }
            listIndex = weeklyList.getSelectedIndex();
            if(listIndex != -1) {  //-1 means all were selected, which is impossible with single_selection, so this basically becomes if anything was selected
                switch (listIndex) {
                    case 0:
                        recommendOutput = "WEEKLY_PREM";  //OT-09
                        break;
                    case 1:
                        recommendOutput = "WINDOW_PREM_WEEK";  //0T-08
                        break;
                    case 2:
                        recommendOutput = "FIXED_PERIOD_PREM";  //OT-22
                        break;
                    case 3:
                        recommendOutput = "CONSEC_WORKED_DAYS_PREM";  //OT-16
                        break;
                    case 4:
                        recommendOutput = "WEEKLY_PREM_GREATER_SCHEDULE";  //OT-10
                        break;
                    case 5:
                        recommendOutput = "CONSEC_UNSCHED_DAYS";  //OT-13
                        break;
                    case 6:
                        recommendOutput = "CONSEC_WORKWEEK_7TH_DAY";  //OT-02
                        break;
                    case 7:
                        recommendOutput = "CONSEC_WORKED_DAYS_ROLLING";  //OT-01
                        break;
                    case 8:
                        recommendOutput = "WEEKLY_OT_SPLIT_WEEK";  //OT-21
                        break;
                    case 9:
                        recommendOutput = "NINE_EIGHTY";  //SP-29 (should change SP to OT
                        break;
                    default:
                        recommendOutput = recOutputPlaceholder;
                        break;
                }
            }
            recOutputLabel.setText(recommendOutput);
        }
    }
    
    class dailySelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if(!isChanging) {  //clears choices made in other lists, prior to selection made in this list. Prevents lists with one choice where choice has already been made from being un-selectable
                isChanging = true;        
                withAccrualsList.clearSelection();
                withoutAccrualsList.clearSelection();
                miscAccrualsList.clearSelection();
                exceptionsList.clearSelection();
                resultsList.clearSelection();
                dayList.clearSelection();
                methodList.clearSelection();
                weeklyList.clearSelection();
                restList.clearSelection();
                region1FederalList.clearSelection();                
                region1AKList.clearSelection();
                region1CAList.clearSelection();
                region1COList.clearSelection();
                region1CTList.clearSelection();
                region1KSList.clearSelection();
                region1KYList.clearSelection();
                region1MAList.clearSelection();
                region1MNList.clearSelection();
                region1NVList.clearSelection();
                region1NJList.clearSelection();
                region1NYList.clearSelection();
                region1ORList.clearSelection();
                region1PAList.clearSelection();
                region1PRList.clearSelection();
                region1RIList.clearSelection();
                region1WAList.clearSelection();
                region1DCList.clearSelection();
                region1WIList.clearSelection();
                region2List.clearSelection();
                region3List.clearSelection();
                isChanging = false;
            }
            listIndex = dailyList.getSelectedIndex();
            if(listIndex != -1) {  //-1 means all were selected, which is impossible with single_selection, so this basically becomes if anything was selected
                switch (listIndex) {
                    case 0:
                        recommendOutput = "DAILY_PREM_HOURS";  //OT-05
                        break;
                    case 1:
                        recommendOutput = "DAILY_PREM_ROLLING";  //OT-07
                        break;
                    case 2:
                        recommendOutput = "WINDOW_PREM_24_HRS";  //OT-03
                        break;
                    case 3:
                        recommendOutput = "SHIFT_PREM_HOUR";  //OT-25
                        break;
                    case 4:
                        recommendOutput = "WINDOW_PREM_RANGE";  //OT-11
                        break;
                    case 5:
                        recommendOutput = "DAILY_PREM_OUTSIDE_SCHEDULE";  //OT-06
                        break;
                    case 6:
                        recommendOutput = "DAILY_PREM_GREATER_SCHEDULE";  //OT-04
                        break;
                    case 7:
                        recommendOutput = "DAILY_PREM_UNSCHED";  //OT-14
                        break;
                    default:
                        recommendOutput = recOutputPlaceholder;
                        break;
                }
            }
            recOutputLabel.setText(recommendOutput);
        }
    }
    
    class restSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent le) {
            if(!isChanging) {  //clears choices made in other lists, prior to selection made in this list. Prevents lists with one choice where choice has already been made from being un-selectable
                isChanging = true;        
                withAccrualsList.clearSelection();
                withoutAccrualsList.clearSelection();
                miscAccrualsList.clearSelection();
                exceptionsList.clearSelection();
                resultsList.clearSelection();
                dayList.clearSelection();
                methodList.clearSelection();
                weeklyList.clearSelection();
                dailyList.clearSelection();
                region1FederalList.clearSelection();                
                region1AKList.clearSelection();
                region1CAList.clearSelection();
                region1COList.clearSelection();
                region1CTList.clearSelection();
                region1KSList.clearSelection();
                region1KYList.clearSelection();
                region1MAList.clearSelection();
                region1MNList.clearSelection();
                region1NVList.clearSelection();
                region1NJList.clearSelection();
                region1NYList.clearSelection();
                region1ORList.clearSelection();
                region1PAList.clearSelection();
                region1PRList.clearSelection();
                region1RIList.clearSelection();
                region1WAList.clearSelection();
                region1DCList.clearSelection();
                region1WIList.clearSelection();
                region2List.clearSelection();
                region3List.clearSelection();
                isChanging = false;
            }
            listIndex = restList.getSelectedIndex();
            if(listIndex != -1) {  //-1 means all were selected, which is impossible with single_selection, so this basically becomes if anything was selected
                switch (listIndex) {
                    case 0:
                        recommendOutput = "REST_PREM_OVERLAP";  //OT-12
                        break;
                    case 1:
                        recommendOutput = "REST_PREM_ON_OVERLAP";  //OT-15
                        break;
                    case 2:
                        recommendOutput = "REST_TCP_MISSED_AMOUNT";  //OT-17
                        break;
                    case 3:
                        recommendOutput = "REST_TCP_FIXED_AMOUNT";  //OT-18
                        break;
                    case 4:
                        recommendOutput = "REST_TCP_OVERLAP";  //OT-19
                        break;
                    case 5:
                        recommendOutput = "REST_TCP_ON_OVERLAP";  //OT-20
                        break;
                    default:
                        recommendOutput = recOutputPlaceholder;
                        break;
                }
            }
            recOutputLabel.setText(recommendOutput);
        }
    }

    class region1FederalListSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent le) {
            if(!isChanging) {  //clears choices made in other lists, prior to selection made in this list. Prevents lists with one choice where choice has already been made from being un-selectable
                isChanging = true;
                withAccrualsList.clearSelection();
                withoutAccrualsList.clearSelection();
                miscAccrualsList.clearSelection();
                exceptionsList.clearSelection();
                resultsList.clearSelection();
                dayList.clearSelection();
                methodList.clearSelection();
                weeklyList.clearSelection();
                dailyList.clearSelection();
                restList.clearSelection();
                region1AKList.clearSelection();
                region1CAList.clearSelection();
                region1COList.clearSelection();
                region1CTList.clearSelection();
                region1KSList.clearSelection();
                region1KYList.clearSelection();
                region1MAList.clearSelection();
                region1MNList.clearSelection();
                region1NVList.clearSelection();
                region1NJList.clearSelection();
                region1NYList.clearSelection();
                region1ORList.clearSelection();
                region1PAList.clearSelection();
                region1PRList.clearSelection();
                region1RIList.clearSelection();
                region1WAList.clearSelection();
                region1DCList.clearSelection();
                region1WIList.clearSelection();
                region2List.clearSelection();
                region3List.clearSelection();
                isChanging = false;
            }
            listIndex = region1FederalList.getSelectedIndex();
            if(listIndex != -1) {  //-1 means all were selected, which is impossible with single_selection, so this basically becomes if anything was selected
                switch (listIndex) {
                    case 0:
                        recommendOutput = "FLSA_WEEKLY  or  FLSA_WEEKLY_MA";
                        break;
                    case 1:
                        recommendOutput = "FLSA_WEEKLY_BLENDED";
                        break;
                    case 2:
                        recommendOutput = "FMLA_FIRST_USE";
                        break;
                    case 3:
                        recommendOutput = "FMLA_MIL";
                        break;
                    case 4:
                        recommendOutput = "FMLA_ROLLING  or  FMLA_ROLLING_MA";
                        break;
                    case 5:
                        recommendOutput = "FMLA_YEARLY";
                        break;
                     case 6:
                        recommendOutput = "FMLA_FIXED_DATE";
                        break;
                    default:
                        recommendOutput = recOutputPlaceholder;
                        break;
                }
            }
            recOutputLabel.setText(recommendOutput);
        }
    }
   
    class region1AKListSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent le) {
            if(!isChanging) {  //clears choices made in other lists, prior to selection made in this list. Prevents lists with one choice where choice has already been made from being un-selectable
                isChanging = true;
                withAccrualsList.clearSelection();
                withoutAccrualsList.clearSelection();
                miscAccrualsList.clearSelection();
                exceptionsList.clearSelection();
                resultsList.clearSelection();
                dayList.clearSelection();
                methodList.clearSelection();
                weeklyList.clearSelection();
                dailyList.clearSelection();
                restList.clearSelection();                
                region1FederalList.clearSelection();
                region1CAList.clearSelection();
                region1COList.clearSelection();
                region1CTList.clearSelection();
                region1KSList.clearSelection();
                region1KYList.clearSelection();
                region1MAList.clearSelection();
                region1MNList.clearSelection();
                region1NVList.clearSelection();
                region1NJList.clearSelection();
                region1NYList.clearSelection();
                region1ORList.clearSelection();
                region1PAList.clearSelection();
                region1PRList.clearSelection();
                region1RIList.clearSelection();
                region1WAList.clearSelection();
                region1DCList.clearSelection();
                region1WIList.clearSelection();
                region2List.clearSelection();
                region3List.clearSelection();            
                isChanging = false;
            }
            listIndex = region1AKList.getSelectedIndex();
                if(listIndex != -1) {  //-1 means all were selected, which is impossible with single_selection, so this basically becomes if anything was selected
                    switch (listIndex) {
                        case 0:
                        recommendOutput = "AK_LAWS";
                        break;
                   default:
                        recommendOutput = recOutputPlaceholder;
                        break;
                }
            }
            recOutputLabel.setText(recommendOutput);
        }
    }
    
    class region1CAListSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent le) {
            if(!isChanging) {  //clears choices made in other lists, prior to selection made in this list. Prevents lists with one choice where choice has already been made from being un-selectable
                isChanging = true;
                withAccrualsList.clearSelection();
                withoutAccrualsList.clearSelection();
                miscAccrualsList.clearSelection();
                exceptionsList.clearSelection();
                resultsList.clearSelection();
                dayList.clearSelection();
                methodList.clearSelection();
                weeklyList.clearSelection();
                dailyList.clearSelection();
                restList.clearSelection();
                region1FederalList.clearSelection();
                region1AKList.clearSelection();
                region1COList.clearSelection();
                region1CTList.clearSelection();
                region1KSList.clearSelection();
                region1KYList.clearSelection();
                region1MAList.clearSelection();
                region1MNList.clearSelection();
                region1NVList.clearSelection();
                region1NJList.clearSelection();
                region1NYList.clearSelection();
                region1ORList.clearSelection();
                region1PAList.clearSelection();
                region1PRList.clearSelection();
                region1RIList.clearSelection();
                region1WAList.clearSelection();
                region1DCList.clearSelection();
                region1WIList.clearSelection();
                region2List.clearSelection();
                region3List.clearSelection();
                isChanging = false;
            }    
            listIndex = region1CAList.getSelectedIndex();
            if(listIndex != -1) {  //-1 means all were selected, which is impossible with single_selection, so this basically becomes if anything was selected
                switch (listIndex) {
                    case 0:
                        recommendOutput = "CA_LAWS_OT  or  CA_LAWS_OT_MA";
                        break;
                    case 1:
                        recommendOutput = "CA_LAWS_ALT  or  CA_LAWS_ALT_MA";
                        break;
                    case 2:
                        recommendOutput = "CA_SICK_ACCRUAL";
                        break;
                    case 3:
                        recommendOutput = "CA_LAWS_MAKE_UP";
                        break;
                    case 4:
                        recommendOutput = "CFRA";
                        break;
                    case 5:
                        recommendOutput = "CA_LAWS_MEAL  or  CA_LAWS_MEAL_MA";
                        break;
                    case 6:
                        recommendOutput = "CA_LAWS_MEAL_TCP  or  CA_LAWS_MEAL_TCP_MA";
                        break;
                    case 7:
                        recommendOutput = "CA_LAWS_REST_BREAK";
                        break;
                    case 8:
                        recommendOutput = "OAKLAND_SICK_ACCRUAL";
                        break;
                    case 9:
                        recommendOutput = "SAN_FRANCISCO_SICK_ACCRUAL";
                        break;
                    case 10:
                        recommendOutput = "SAN_DIEGO_SICK_ACCRUAL";
                        break;
                    default:
                        recommendOutput = recOutputPlaceholder;
                        break;
                }
            }
            recOutputLabel.setText(recommendOutput);
        }
    }
    
    class region1COListSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent le) {
            if(!isChanging) {  //clears choices made in other lists, prior to selection made in this list. Prevents lists with one choice where choice has already been made from being un-selectable
                isChanging = true;
              	withAccrualsList.clearSelection();
                withoutAccrualsList.clearSelection();
                miscAccrualsList.clearSelection();
                exceptionsList.clearSelection();
                resultsList.clearSelection();
                dayList.clearSelection();
                methodList.clearSelection();
                weeklyList.clearSelection();
                dailyList.clearSelection();
                restList.clearSelection();
                region1FederalList.clearSelection();
                region1AKList.clearSelection();
                region1CAList.clearSelection();
                region1CTList.clearSelection();
                region1KSList.clearSelection();
                region1KYList.clearSelection();
                region1MAList.clearSelection();
                region1MNList.clearSelection();
                region1NVList.clearSelection();
                region1NJList.clearSelection();
                region1NYList.clearSelection();
                region1ORList.clearSelection();
                region1PAList.clearSelection();
                region1PRList.clearSelection();
                region1RIList.clearSelection();
                region1WAList.clearSelection();
                region1DCList.clearSelection();
                region1WIList.clearSelection();
                region2List.clearSelection();
                region3List.clearSelection();
                isChanging = false;
            }
            listIndex = region1COList.getSelectedIndex();
            if(listIndex != -1) {  //-1 means all were selected, which is impossible with single_selection, so this basically becomes if anything was selected
                switch (listIndex) {
                    case 0:
                        recommendOutput = "CO_LAWS";
                        break;
                    default:
                        recommendOutput = recOutputPlaceholder;
                        break;
                }
            }
            recOutputLabel.setText(recommendOutput);
        }
    }
    
    class region1CTListSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent le) {
            if(!isChanging) {  //clears choices made in other lists, prior to selection made in this list. Prevents lists with one choice where choice has already been made from being un-selectable
                isChanging = true;
                withAccrualsList.clearSelection();
                withoutAccrualsList.clearSelection();
                miscAccrualsList.clearSelection();
                exceptionsList.clearSelection();
                resultsList.clearSelection();
                dayList.clearSelection();
                methodList.clearSelection();
                weeklyList.clearSelection();
                dailyList.clearSelection();
                restList.clearSelection();
                region1FederalList.clearSelection();
                region1AKList.clearSelection();
                region1CAList.clearSelection();
                region1COList.clearSelection();
                region1KSList.clearSelection();
                region1KYList.clearSelection();
                region1MAList.clearSelection();
                region1MNList.clearSelection();
                region1NVList.clearSelection();
                region1NJList.clearSelection();
                region1NYList.clearSelection();
                region1ORList.clearSelection();
                region1PAList.clearSelection();
                region1PRList.clearSelection();
                region1RIList.clearSelection();
                region1WAList.clearSelection();
                region1DCList.clearSelection();
                region1WIList.clearSelection();
                region2List.clearSelection();
                region3List.clearSelection();
                isChanging = false;
            }    
            listIndex = region1CTList.getSelectedIndex();
            if(listIndex != -1) {  //-1 means all were selected, which is impossible with single_selection, so this basically becomes if anything was selected
                switch (listIndex) {
                    case 0:
                        recommendOutput = "CONNECTICUT_SICK_ACCRUAL";
                        break;
                    default:
                        recommendOutput = recOutputPlaceholder;
                        break;
                }
            }
            recOutputLabel.setText(recommendOutput);
        }
    }
    
    class region1KSListSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent le) {
            if(!isChanging) {  //clears choices made in other lists, prior to selection made in this list. Prevents lists with one choice where choice has already been made from being un-selectable
                isChanging = true;
                withAccrualsList.clearSelection();
                withoutAccrualsList.clearSelection();
                miscAccrualsList.clearSelection();
                exceptionsList.clearSelection();
                resultsList.clearSelection();
                dayList.clearSelection();
                methodList.clearSelection();
                weeklyList.clearSelection();
                dailyList.clearSelection();
                restList.clearSelection();
                region1FederalList.clearSelection();
                region1AKList.clearSelection();
                region1CAList.clearSelection();
                region1COList.clearSelection();
                region1CTList.clearSelection();
                region1KYList.clearSelection();
                region1MAList.clearSelection();
                region1MNList.clearSelection();
                region1NVList.clearSelection();
                region1NJList.clearSelection();
                region1NYList.clearSelection();
                region1ORList.clearSelection();
                region1PAList.clearSelection();
                region1PRList.clearSelection();
                region1RIList.clearSelection();
                region1WAList.clearSelection();
                region1DCList.clearSelection();
                region1WIList.clearSelection();
                region2List.clearSelection();
                region3List.clearSelection();
                isChanging = false;
            }    
            listIndex = region1KSList.getSelectedIndex();
            if(listIndex != -1) {  //-1 means all were selected, which is impossible with single_selection, so this basically becomes if anything was selected
                switch (listIndex) {
                    case 0:
                        recommendOutput = "KS_LAWS";
                        break;
                    default:
                        recommendOutput = recOutputPlaceholder;
                        break;
                }
            }
            recOutputLabel.setText(recommendOutput);
        }
    }    
    
    class region1KYListSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent le) {
            if(!isChanging) {  //clears choices made in other lists, prior to selection made in this list. Prevents lists with one choice where choice has already been made from being un-selectable
                isChanging = true;
                withAccrualsList.clearSelection();
                withoutAccrualsList.clearSelection();
                miscAccrualsList.clearSelection();
                exceptionsList.clearSelection();
                resultsList.clearSelection();
                dayList.clearSelection();
                methodList.clearSelection();
                weeklyList.clearSelection();
                dailyList.clearSelection();
                restList.clearSelection();
                region1FederalList.clearSelection();
                region1AKList.clearSelection();
                region1CAList.clearSelection();
                region1COList.clearSelection();
                region1CTList.clearSelection();
                region1KSList.clearSelection();
                region1MAList.clearSelection();
                region1MNList.clearSelection();
                region1NVList.clearSelection();
                region1NJList.clearSelection();
                region1NYList.clearSelection();
                region1ORList.clearSelection();
                region1PAList.clearSelection();
                region1PRList.clearSelection();
                region1RIList.clearSelection();
                region1WAList.clearSelection();
                region1DCList.clearSelection();
                region1WIList.clearSelection();
                region2List.clearSelection();
                region3List.clearSelection();
                isChanging = false;
            }
            listIndex = region1KYList.getSelectedIndex();
            if(listIndex != -1) {  //-1 means all were selected, which is impossible with single_selection, so this basically becomes if anything was selected
                switch (listIndex) {
                    case 0:
                        recommendOutput = "KY_LAWS";
                        break;    
                    default:
                        recommendOutput = recOutputPlaceholder;
                        break;
                }
            }
            recOutputLabel.setText(recommendOutput);
        }
    }    
    
    class region1MAListSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent le) {
            if(!isChanging) {  //clears choices made in other lists, prior to selection made in this list. Prevents lists with one choice where choice has already been made from being un-selectable
                isChanging = true;
                withAccrualsList.clearSelection();
                withoutAccrualsList.clearSelection();
                miscAccrualsList.clearSelection();
                exceptionsList.clearSelection();
                resultsList.clearSelection();
                dayList.clearSelection();
                methodList.clearSelection();
                weeklyList.clearSelection();
                dailyList.clearSelection();
                restList.clearSelection();                
                region1FederalList.clearSelection();
                region1AKList.clearSelection();
                region1CAList.clearSelection();
                region1COList.clearSelection();
                region1CTList.clearSelection();
                region1KSList.clearSelection();
                region1KYList.clearSelection();
                region1MNList.clearSelection();
                region1NVList.clearSelection();
                region1NJList.clearSelection();
                region1NYList.clearSelection();
                region1ORList.clearSelection();
                region1PAList.clearSelection();
                region1PRList.clearSelection();
                region1RIList.clearSelection();
                region1WAList.clearSelection();
                region1DCList.clearSelection();
                region1WIList.clearSelection();
                region2List.clearSelection();
                region3List.clearSelection();
                isChanging = false;
            }    
            listIndex = region1MAList.getSelectedIndex();
            if(listIndex != -1) {  //-1 means all were selected, which is impossible with single_selection, so this basically becomes if anything was selected
                switch (listIndex) {
                    case 0:
                        recommendOutput = "MASSACHUSETTS_SICK_ACCRUAL";
                        break;          
                    default:
                        recommendOutput = recOutputPlaceholder;
                        break;
                }
            }
            recOutputLabel.setText(recommendOutput);
        }
    }    
    
    class region1MNListSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent le) {
            if(!isChanging) {  //clears choices made in other lists, prior to selection made in this list. Prevents lists with one choice where choice has already been made from being un-selectable
                isChanging = true;
                withAccrualsList.clearSelection();
                withoutAccrualsList.clearSelection();
                miscAccrualsList.clearSelection();
                exceptionsList.clearSelection();
                resultsList.clearSelection();
                dayList.clearSelection();
                methodList.clearSelection();
                weeklyList.clearSelection();
                dailyList.clearSelection();
                restList.clearSelection();                   
                region1FederalList.clearSelection();
                region1AKList.clearSelection();
                region1CAList.clearSelection();
                region1COList.clearSelection();
                region1CTList.clearSelection();
                region1KSList.clearSelection();
                region1KYList.clearSelection();
                region1MAList.clearSelection();
                region1NVList.clearSelection();
                region1NJList.clearSelection();
                region1NYList.clearSelection();
                region1ORList.clearSelection();
                region1PAList.clearSelection();
                region1PRList.clearSelection();
                region1RIList.clearSelection();
                region1WAList.clearSelection();
                region1DCList.clearSelection();
                region1WIList.clearSelection();
                region2List.clearSelection();
                region3List.clearSelection();
                isChanging = false;
            }    
            listIndex = region1MNList.getSelectedIndex();
            if(listIndex != -1) {  //-1 means all were selected, which is impossible with single_selection, so this basically becomes if anything was selected
                switch (listIndex) {
                    case 0:
                        recommendOutput = "MN_LAWS";
                        break;     
                    default:
                        recommendOutput = recOutputPlaceholder;
                        break;
                }
            }
            recOutputLabel.setText(recommendOutput);
        }
    }    
    
    class region1NVListSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent le) {
            if(!isChanging) {  //clears choices made in other lists, prior to selection made in this list. Prevents lists with one choice where choice has already been made from being un-selectable
                isChanging = true;
                withAccrualsList.clearSelection();
                withoutAccrualsList.clearSelection();
                miscAccrualsList.clearSelection();
                exceptionsList.clearSelection();
                resultsList.clearSelection();
                dayList.clearSelection();
                methodList.clearSelection();
                weeklyList.clearSelection();
                dailyList.clearSelection();
                restList.clearSelection();                   
                region1FederalList.clearSelection();
                region1AKList.clearSelection();
                region1CAList.clearSelection();
                region1COList.clearSelection();
                region1CTList.clearSelection();
                region1KSList.clearSelection();
                region1KYList.clearSelection();
                region1MAList.clearSelection();
                region1MNList.clearSelection();
                region1NJList.clearSelection();
                region1NYList.clearSelection();
                region1ORList.clearSelection();
                region1PAList.clearSelection();
                region1PRList.clearSelection();
                region1RIList.clearSelection();
                region1WAList.clearSelection();
                region1DCList.clearSelection();
                region1WIList.clearSelection();
                region2List.clearSelection();
                region3List.clearSelection();
                isChanging = false;
            }    
            listIndex = region1NVList.getSelectedIndex();
            if(listIndex != -1) {  //-1 means all were selected, which is impossible with single_selection, so this basically becomes if anything was selected
                switch (listIndex) {
                    case 0:
                        recommendOutput = "NV_LAWS";
                        break;                
                    default:
                        recommendOutput = recOutputPlaceholder;
                        break;
                }
            }
            recOutputLabel.setText(recommendOutput);
        }
    }    
    
    class region1NJListSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent le) {
            if(!isChanging) {  //clears choices made in other lists, prior to selection made in this list. Prevents lists with one choice where choice has already been made from being un-selectable
                isChanging = true;
                withAccrualsList.clearSelection();
                withoutAccrualsList.clearSelection();
                miscAccrualsList.clearSelection();
                exceptionsList.clearSelection();
                resultsList.clearSelection();
                dayList.clearSelection();
                methodList.clearSelection();
                weeklyList.clearSelection();
                dailyList.clearSelection();
                restList.clearSelection();                   
                region1FederalList.clearSelection();
                region1AKList.clearSelection();
                region1CAList.clearSelection();
                region1COList.clearSelection();
                region1CTList.clearSelection();
                region1KSList.clearSelection();
                region1KYList.clearSelection();
                region1MAList.clearSelection();
                region1MNList.clearSelection();
                region1NVList.clearSelection();
                region1NYList.clearSelection();
                region1ORList.clearSelection();
                region1PAList.clearSelection();
                region1PRList.clearSelection();
                region1RIList.clearSelection();
                region1WAList.clearSelection();
                region1DCList.clearSelection();
                region1WIList.clearSelection();
                region2List.clearSelection();
                region3List.clearSelection();
                isChanging = false;
            }
            listIndex = region1NJList.getSelectedIndex();
            if(listIndex != -1) {  //-1 means all were selected, which is impossible with single_selection, so this basically becomes if anything was selected
                switch (listIndex) {
                    case 0:
                        recommendOutput = "JERSEY_CITY_SICK_ACCRUAL";
                        break;
                    case 1:
                        recommendOutput = "NEWARK_SICK_ACCRUAL";
                        break;
                    default:
                        recommendOutput = recOutputPlaceholder;
                        break;
                }
            }
            recOutputLabel.setText(recommendOutput);
        }
    }    
    
    class region1NYListSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent le) {
            if(!isChanging) {  //clears choices made in other lists, prior to selection made in this list. Prevents lists with one choice where choice has already been made from being un-selectable
                isChanging = true;
                withAccrualsList.clearSelection();
                withoutAccrualsList.clearSelection();
                miscAccrualsList.clearSelection();
                exceptionsList.clearSelection();
                resultsList.clearSelection();
                dayList.clearSelection();
                methodList.clearSelection();
                weeklyList.clearSelection();
                dailyList.clearSelection();
                restList.clearSelection();                   
                region1FederalList.clearSelection();
                region1AKList.clearSelection();
                region1CAList.clearSelection();
                region1COList.clearSelection();
                region1CTList.clearSelection();
                region1KSList.clearSelection();
                region1KYList.clearSelection();
                region1MAList.clearSelection();
                region1MNList.clearSelection();
                region1NVList.clearSelection();
                region1NJList.clearSelection();
                region1ORList.clearSelection();
                region1PAList.clearSelection();
                region1PRList.clearSelection();
                region1RIList.clearSelection();
                region1WAList.clearSelection();
                region1DCList.clearSelection();
                region1WIList.clearSelection();
                region2List.clearSelection();
                region3List.clearSelection();
                isChanging = false;
            }    
            listIndex = region1NYList.getSelectedIndex();
            if(listIndex != -1) {  //-1 means all were selected, which is impossible with single_selection, so this basically becomes if anything was selected
                switch (listIndex) {
                    case 0:
                        recommendOutput = "NYC_SICK_ACCRUAL";
                        break;
                    case 1:
                        recommendOutput = "NY_LAWS_MEAL";
                        break;
                    default:
                        recommendOutput = recOutputPlaceholder;
                        break;
                }
            }
            recOutputLabel.setText(recommendOutput);
        }
    }    
    
    class region1ORListSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent le) {
            if(!isChanging) {  //clears choices made in other lists, prior to selection made in this list. Prevents lists with one choice where choice has already been made from being un-selectable
                isChanging = true;
                withAccrualsList.clearSelection();
                withoutAccrualsList.clearSelection();
                miscAccrualsList.clearSelection();
                exceptionsList.clearSelection();
                resultsList.clearSelection();
                dayList.clearSelection();
                methodList.clearSelection();
                weeklyList.clearSelection();
                dailyList.clearSelection();
                restList.clearSelection();                   
                region1FederalList.clearSelection();
                region1AKList.clearSelection();
                region1CAList.clearSelection();
                region1COList.clearSelection();
                region1CTList.clearSelection();
                region1KSList.clearSelection();
                region1KYList.clearSelection();
                region1MAList.clearSelection();
                region1MNList.clearSelection();
                region1NVList.clearSelection();
                region1NJList.clearSelection();
                region1NYList.clearSelection();
                region1PAList.clearSelection();
                region1PRList.clearSelection();
                region1RIList.clearSelection();
                region1WAList.clearSelection();
                region1DCList.clearSelection();
                region1WIList.clearSelection();
                region2List.clearSelection();
                region3List.clearSelection();
                isChanging = false;
            }
            listIndex = region1ORList.getSelectedIndex();
            if(listIndex != -1) {  //-1 means all were selected, which is impossible with single_selection, so this basically becomes if anything was selected
                switch (listIndex) {
                    case 0:
                        recommendOutput = "EUGENE_SICK_ACCRUAL";
                        break;
                    case 1:
                        recommendOutput = "PORTLAND_SICK_ACCRUAL";
                        break;
                    default:
                        recommendOutput = recOutputPlaceholder;
                        break;
                }
            }
            recOutputLabel.setText(recommendOutput);
        }
    }    
    
    class region1PAListSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent le) {
            if(!isChanging) {  //clears choices made in other lists, prior to selection made in this list. Prevents lists with one choice where choice has already been made from being un-selectable
                isChanging = true;
                withAccrualsList.clearSelection();
                withoutAccrualsList.clearSelection();
                miscAccrualsList.clearSelection();
                exceptionsList.clearSelection();
                resultsList.clearSelection();
                dayList.clearSelection();
                methodList.clearSelection();
                weeklyList.clearSelection();
                dailyList.clearSelection();
                restList.clearSelection();                   
                region1FederalList.clearSelection();
                region1AKList.clearSelection();
                region1CAList.clearSelection();
                region1COList.clearSelection();
                region1CTList.clearSelection();
                region1KSList.clearSelection();
                region1KYList.clearSelection();
                region1MAList.clearSelection();
                region1MNList.clearSelection();
                region1NVList.clearSelection();
                region1NJList.clearSelection();
                region1NYList.clearSelection();
                region1ORList.clearSelection();
                region1PRList.clearSelection();
                region1RIList.clearSelection();
                region1WAList.clearSelection();
                region1DCList.clearSelection();
                region1WIList.clearSelection();
                region2List.clearSelection();
                region3List.clearSelection();
                isChanging = false;
            }
            listIndex = region1PAList.getSelectedIndex();
            if(listIndex != -1) {  //-1 means all were selected, which is impossible with single_selection, so this basically becomes if anything was selected
                switch (listIndex) {
                    case 0:
                        recommendOutput = "PHILADELPHIA_SICK_ACCRUAL";
                        break;
                    default:
                        recommendOutput = recOutputPlaceholder;
                        break;
                }
            }
            recOutputLabel.setText(recommendOutput);
        }
    }    
    
    class region1PRListSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent le) {
            if(!isChanging) {  //clears choices made in other lists, prior to selection made in this list. Prevents lists with one choice where choice has already been made from being un-selectable
                isChanging = true;
                withAccrualsList.clearSelection();
                withoutAccrualsList.clearSelection();
                miscAccrualsList.clearSelection();
                exceptionsList.clearSelection();
                resultsList.clearSelection();
                dayList.clearSelection();
                methodList.clearSelection();
                weeklyList.clearSelection();
                dailyList.clearSelection();
                restList.clearSelection();                   
                region1FederalList.clearSelection();
                region1AKList.clearSelection();
                region1CAList.clearSelection();
                region1COList.clearSelection();
                region1CTList.clearSelection();
                region1KSList.clearSelection();
                region1KYList.clearSelection();
                region1MAList.clearSelection();
                region1MNList.clearSelection();
                region1NVList.clearSelection();
                region1NJList.clearSelection();
                region1NYList.clearSelection();
                region1ORList.clearSelection();
                region1PAList.clearSelection();
                region1RIList.clearSelection();
                region1WAList.clearSelection();
                region1DCList.clearSelection();
                region1WIList.clearSelection();
                region2List.clearSelection();
                region3List.clearSelection();
                isChanging = false;
            }
            listIndex = region1PRList.getSelectedIndex();
            if(listIndex != -1) {  //-1 means all were selected, which is impossible with single_selection, so this basically becomes if anything was selected
                switch (listIndex) {
                    case 0:
                        recommendOutput = "PR_LAWS";
                        break;
                    case 1:
                        recommendOutput = "PR_LAWS_MEAL";
                        break;
                    default:
                        recommendOutput = recOutputPlaceholder;
                        break;
                }
            }
            recOutputLabel.setText(recommendOutput);
        }
    }    
    
    class region1RIListSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent le) {
            if(!isChanging) {  //clears choices made in other lists, prior to selection made in this list. Prevents lists with one choice where choice has already been made from being un-selectable
                isChanging = true;
                withAccrualsList.clearSelection();
                withoutAccrualsList.clearSelection();
                miscAccrualsList.clearSelection();
                exceptionsList.clearSelection();
                resultsList.clearSelection();
                dayList.clearSelection();
                methodList.clearSelection();
                weeklyList.clearSelection();
                dailyList.clearSelection();
                restList.clearSelection();                   
                region1FederalList.clearSelection();
                region1AKList.clearSelection();
                region1CAList.clearSelection();
                region1COList.clearSelection();
                region1CTList.clearSelection();
                region1KSList.clearSelection();
                region1KYList.clearSelection();
                region1MAList.clearSelection();
                region1MNList.clearSelection();
                region1NVList.clearSelection();
                region1NJList.clearSelection();
                region1NYList.clearSelection();
                region1ORList.clearSelection();
                region1PAList.clearSelection();
                region1PRList.clearSelection();
                region1WAList.clearSelection();
                region1DCList.clearSelection();
                region1WIList.clearSelection();
                region2List.clearSelection();
                region3List.clearSelection();
                isChanging = false;
            }
            listIndex = region1RIList.getSelectedIndex();
            if(listIndex != -1) {  //-1 means all were selected, which is impossible with single_selection, so this basically becomes if anything was selected
                switch (listIndex) {
                    case 0:
                        recommendOutput = "RI_LAWS";
                        break;
                    default:
                        recommendOutput = recOutputPlaceholder;
                        break;
                }
            }
            recOutputLabel.setText(recommendOutput);
        }
    }    
    
    class region1WAListSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent le) {
            if(!isChanging) {  //clears choices made in other lists, prior to selection made in this list. Prevents lists with one choice where choice has already been made from being un-selectable
                isChanging = true;
                withAccrualsList.clearSelection();
                withoutAccrualsList.clearSelection();
                miscAccrualsList.clearSelection();
                exceptionsList.clearSelection();
                resultsList.clearSelection();
                dayList.clearSelection();
                methodList.clearSelection();
                weeklyList.clearSelection();
                dailyList.clearSelection();
                restList.clearSelection();                   
                region1FederalList.clearSelection();
                region1AKList.clearSelection();
                region1CAList.clearSelection();
                region1COList.clearSelection();
                region1CTList.clearSelection();
                region1KSList.clearSelection();
                region1KYList.clearSelection();
                region1MAList.clearSelection();
                region1MNList.clearSelection();
                region1NVList.clearSelection();
                region1NJList.clearSelection();
                region1NYList.clearSelection();
                region1ORList.clearSelection();
                region1PAList.clearSelection();
                region1PRList.clearSelection();
                region1RIList.clearSelection();
                region1DCList.clearSelection();
                region1WIList.clearSelection();
                region2List.clearSelection();
                region3List.clearSelection();
                isChanging = false;
            }
            listIndex = region1WAList.getSelectedIndex();
            if(listIndex != -1) {  //-1 means all were selected, which is impossible with single_selection, so this basically becomes if anything was selected
                switch (listIndex) {
                    case 0:
                        recommendOutput = "SEATTLE_SICK_AND_SAFE";
                        break;
                    default:
                        recommendOutput = recOutputPlaceholder;
                        break;
                }
            }
            recOutputLabel.setText(recommendOutput);
        }
    }    
    
    class region1DCListSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent le) {
            if(!isChanging) {  //clears choices made in other lists, prior to selection made in this list. Prevents lists with one choice where choice has already been made from being un-selectable
                isChanging = true;
                withAccrualsList.clearSelection();
                withoutAccrualsList.clearSelection();
                miscAccrualsList.clearSelection();
                exceptionsList.clearSelection();
                resultsList.clearSelection();
                dayList.clearSelection();
                methodList.clearSelection();
                weeklyList.clearSelection();
                dailyList.clearSelection();
                restList.clearSelection();                   
                region1FederalList.clearSelection();
                region1AKList.clearSelection();
                region1CAList.clearSelection();
                region1COList.clearSelection();
                region1CTList.clearSelection();
                region1KSList.clearSelection();
                region1KYList.clearSelection();
                region1MAList.clearSelection();
                region1MNList.clearSelection();
                region1NVList.clearSelection();
                region1NJList.clearSelection();
                region1NYList.clearSelection();
                region1ORList.clearSelection();
                region1PAList.clearSelection();
                region1PRList.clearSelection();
                region1RIList.clearSelection();
                region1WAList.clearSelection();
                region1WIList.clearSelection();
                region2List.clearSelection();
                region3List.clearSelection();
                isChanging = false;
            }
            listIndex = region1DCList.getSelectedIndex();
            if(listIndex != -1) {  //-1 means all were selected, which is impossible with single_selection, so this basically becomes if anything was selected
                switch (listIndex) {
                    case 0:
                        recommendOutput = "DC_SICK_AND_SAFE";
                        break;
                    default:
                        recommendOutput = recOutputPlaceholder;
                        break;
                }
            }
            recOutputLabel.setText(recommendOutput);
        }
    }    
    
    class region1WIListSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent le) {
            if(!isChanging) {  //clears choices made in other lists, prior to selection made in this list. Prevents lists with one choice where choice has already been made from being un-selectable
                isChanging = true;
                withAccrualsList.clearSelection();
                withoutAccrualsList.clearSelection();
                miscAccrualsList.clearSelection();
                exceptionsList.clearSelection();
                resultsList.clearSelection();
                dayList.clearSelection();
                methodList.clearSelection();
                weeklyList.clearSelection();
                dailyList.clearSelection();
                restList.clearSelection();                   
                region1FederalList.clearSelection();
                region1AKList.clearSelection();
                region1CAList.clearSelection();
                region1COList.clearSelection();
                region1CTList.clearSelection();
                region1KSList.clearSelection();
                region1KYList.clearSelection();
                region1MAList.clearSelection();
                region1MNList.clearSelection();
                region1NVList.clearSelection();
                region1NJList.clearSelection();
                region1NYList.clearSelection();
                region1ORList.clearSelection();
                region1PAList.clearSelection();
                region1PRList.clearSelection();
                region1RIList.clearSelection();
                region1WAList.clearSelection();
                region1DCList.clearSelection();
                region2List.clearSelection();
                region3List.clearSelection();
                isChanging = false;
            }
            listIndex = region1WIList.getSelectedIndex();
            if(listIndex != -1) {  //-1 means all were selected, which is impossible with single_selection, so this basically becomes if anything was selected
                switch (listIndex) {
                    case 0:
                        recommendOutput = "WI_ONE_DAY_IN_SEVEN";
                        break;
                    default:
                        recommendOutput = recOutputPlaceholder;
                        break;
                }
            }
            recOutputLabel.setText(recommendOutput);
        }
    }    
    
    @Override
    public void actionPerformed(ActionEvent e) {  //appends Canada template with proper appendix based on user's drop-down choice
            region2List.clearSelection();  //clears the list's selection if the province is changed
            JComboBox cg = (JComboBox)e.getSource();
            reg2Choice = (String)cg.getSelectedItem();
            switch (reg2Choice) {
                case "Federal workers":
                    region2OutputPrefix = "CAN_FED_";
                    break;
                case "Alberta":
                    region2OutputPrefix = "CAN_AB_";
                    break;
                case "British Columbia":
                    region2OutputPrefix = "CAN_BC_";
                    break;
                case "Manitoba":
                    region2OutputPrefix = "CAN_MB_";
                    break;
                case "New Brunswick":
                    region2OutputPrefix = "CAN_NB_";
                    break;
                case "Newfoundland and Labrador":
                    region2OutputPrefix = "CAN_NL_";
                    break;
                case "Northwest Territories":
                    region2OutputPrefix = "CAN_NT_";
                    break;
                case "Nova Scotia":
                    region2OutputPrefix = "CAN_NS_";
                    break;
                case "Nunavut":
                    region2OutputPrefix = "CAN_NU_";
                    break;
                case "Ontario":
                    region2OutputPrefix = "CAN_ON_";
                    break;
                case "Prince Edward Island":
                    region2OutputPrefix = "CAN_PE_";
                    break;
                case "Quebec":
                    region2OutputPrefix = "CAN_QC_";
                    break;
                case "Yukon":
                    region2OutputPrefix = "CAN_YK_";
                    break;
                case "Saskatchewan":
                    region2OutputPrefix = "CAN_SK_";
                    break;
                default:
                    region2OutputPrefix = "CAN_FED";
                    break;
            }
        }
    
    class region2SelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent le) {
            if(!isChanging) {  //clears choices made in other lists, prior to selection made in this list. Prevents lists with one choice where choice has already been made from being un-selectable
                isChanging = true;        
                withAccrualsList.clearSelection();
                withoutAccrualsList.clearSelection();
                miscAccrualsList.clearSelection();
                exceptionsList.clearSelection();
                resultsList.clearSelection();
                dayList.clearSelection();
                methodList.clearSelection();
                weeklyList.clearSelection();
                dailyList.clearSelection();
                restList.clearSelection();
                region1FederalList.clearSelection();
                region1AKList.clearSelection();
                region1CAList.clearSelection();
                region1COList.clearSelection();
                region1CTList.clearSelection();
                region1KSList.clearSelection();
                region1KYList.clearSelection();
                region1MAList.clearSelection();
                region1MNList.clearSelection();
                region1NVList.clearSelection();
                region1NJList.clearSelection();
                region1NYList.clearSelection();
                region1ORList.clearSelection();
                region1PAList.clearSelection();
                region1PRList.clearSelection();
                region1RIList.clearSelection();
                region1WAList.clearSelection();
                region1DCList.clearSelection();
                region1WIList.clearSelection();
                region3List.clearSelection();
                isChanging = false;
            }            
            listIndex = region2List.getSelectedIndex();
            if(listIndex != -1) {  //-1 means all were selected, which is impossible with single_selection, so this basically becomes if anything was selected
                switch (listIndex) {
                    case 0:
                        recommendOutput = region2OutputPrefix + "LAWS_OT";
                        break;
                    case 1:
                        recommendOutput = region2OutputPrefix + "HOL_PAY";
                        break;
                    default:
                        recommendOutput = recOutputPlaceholder;
                        break;
                }
            }
            recOutputLabel.setText(recommendOutput);    
        }
    }
    
    class region3SelectionHandler implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent le) {
            if(!isChanging) {  //clears choices made in other lists, prior to selection made in this list. Prevents lists with one choice where choice has already been made from being un-selectable
                isChanging = true;        
                withAccrualsList.clearSelection();
                withoutAccrualsList.clearSelection();
                miscAccrualsList.clearSelection();
                exceptionsList.clearSelection();
                resultsList.clearSelection();
                dayList.clearSelection();
                methodList.clearSelection();
                weeklyList.clearSelection();
                dailyList.clearSelection();
                restList.clearSelection();
                region1FederalList.clearSelection();
                region1AKList.clearSelection();
                region1CAList.clearSelection();
                region1COList.clearSelection();
                region1CTList.clearSelection();
                region1KSList.clearSelection();
                region1KYList.clearSelection();
                region1MAList.clearSelection();
                region1MNList.clearSelection();
                region1NVList.clearSelection();
                region1NJList.clearSelection();
                region1NYList.clearSelection();
                region1ORList.clearSelection();
                region1PAList.clearSelection();
                region1PRList.clearSelection();
                region1RIList.clearSelection();
                region1WAList.clearSelection();
                region1DCList.clearSelection();
                region1WIList.clearSelection();
                region2List.clearSelection();
                isChanging = false;
            }            
            listIndex = region3List.getSelectedIndex();
            if(listIndex != -1) {  //-1 means all were selected, which is impossible with single_selection, so this basically becomes if anything was selected
                switch (listIndex) {
                    case 0:
                        recommendOutput = "EU_ANNUAL_HOURS";
                        break;
                    case 1:
                        recommendOutput = "EU_FLEXI_TIME_IO";
                        break;
                    case 2:
                        recommendOutput = "EU_FLEXI_TIME_EL";
                        break;
                    case 3:
                        recommendOutput = "EU_DRIVING_REST";
                        break;
                    case 5:
                        recommendOutput = "UK_WTD_REST_BREAK";
                        break;
                    case 6:
                        recommendOutput = "UK_WTD_DAILY_REST";
                        break;
                    case 7:
                        recommendOutput = "UK_WTD_WEEKLY_REST";
                        break;
                    case 8:
                        recommendOutput = "UK_WTD_WEEKLY_WORKING_TIME";
                        break;
                    case 9:
                        recommendOutput = "UK_WTD_WEEKLY_WORKING_TIME_R";
                        break;
                    case 10:    
                        recommendOutput = "UK_WTD_NIGHT_WORK";
                        break;
                    case 12:  //index 11 is a line breaks
                        recommendOutput = "UK_WTD_YOUNG_REST_BREAK";
                        break;
                    case 13:
                        recommendOutput = "UK_WTD_YOUNG_DAILY_REST";
                        break;
                    case 14:
                        recommendOutput = "UK_WTD_YOUNG_WEEKLY_REST";
                        break;
                    case 15:
                        recommendOutput = "UK_WTD_YOUNG_WEEKLY_WORKING_TIME";
                        break;
                    default:
                        recommendOutput = recOutputPlaceholder;
                        break;
                }
            }
            recOutputLabel.setText(recommendOutput);
        }
    }

    public static void createAndShowGUI() {  //class that packs and sets properaties of GUI
        JFrame frame = new JFrame("Template Recommendation Tool - Templates 19.0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        TemplateRecTool tool = new TemplateRecTool();
        tool.addComponentToPane(frame.getContentPane());
        
        frame.pack();
        frame.setLayout(new FlowLayout());
        frame.setSize(1150, 480); //1150 is the lowest width number to use to fit all three lists from shift pay, 480 is the lowest height to fit the lists and recBox nicely
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if( "Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            
        }  
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
}