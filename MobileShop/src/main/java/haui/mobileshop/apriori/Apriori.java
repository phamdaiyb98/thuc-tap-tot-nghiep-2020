package haui.mobileshop.apriori;

import haui.mobileshop.apriori.entity.ConfidenceItemSet;
import haui.mobileshop.apriori.entity.ItemSets;
import haui.mobileshop.apriori.entity.RawData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Apriori {

    private double minSupp;
    private double minConf;
    private int totalTransaction;
    private List<ItemSets> firstCandidate = new ArrayList<>(); // C1
    private List<ItemSets> resultFrequent = new ArrayList<>();
    private List<RawData> rawDataList; // rootData (Transaction Database)

    public void setData(double minSupp, double minConf, int totalTransaction, List<RawData> data) {
        this.minSupp = minSupp;
        this.minConf = minConf;
        this.totalTransaction = totalTransaction;
        rawDataList = new ArrayList<>();
        rawDataList.addAll(data);
    }

    //Get Result
    public List<ConfidenceItemSet> getAllConfidenceItemSet(Set<String> itemX) {
        execute();
        return getListConfidenceItem(resultFrequent, itemX);
    } // lay ra list itemset voi do tin cay chua kiem tra

    public Set<ConfidenceItemSet> getAllConfidenceItemSetFiltered(Set<String> itemX) {
        execute();
        return filterItemSetConfidence(getListConfidenceItem(resultFrequent, itemX));
    } // lay ra list itemset voi do tin cay > minConf

    //Private Methods
    private void genFirstCandidate() {
        Set<String> setString = new HashSet<>();
        rawDataList.forEach(rawData -> {
            setString.addAll(rawData.getItems());// filter get 1-itemset
        });
        setString.forEach(s -> {
            // tim trong db -> tinh do ho tro loai bo cac phan tu < minSupp
            ItemSets itemSets = new ItemSets();
            Set<String> itemSet = new HashSet<>();
            itemSet.add(s); //set itemset

            itemSets.setItems(itemSet);
            itemSets.setSupport(calculateSupportItem(itemSet)); // set support
            firstCandidate.add(itemSets);
        });
    } // tao ra tap ung cu vien dau tien

    private double calculateSupportItem(Set<String> itemSet) { // ham tinh do ho tro cua tung item
        try {
            double count = 0;
            for (RawData rawData : rawDataList) {
                if (rawData.getItems().containsAll(itemSet)) {
                    count++;
                }// kiem tra xem itemSet co phai tap con cua tap du lieu ban dau hay khong
            }
            return count / totalTransaction; // tinh do ho tro
        } catch (ArithmeticException e) {
            e.printStackTrace();
        }
        return 0;
    } // tinh toan do ho tro cua itemSet trong rawData

    private List<ItemSets> filterItemSetSupport(List<ItemSets> candidate) {
        List<ItemSets> filterItemSet = new ArrayList<>();
        candidate.forEach(itemSet -> {
            if (itemSet.getSupport() >= minSupp) {
                filterItemSet.add(itemSet);
            }
        }); // loc itemset candidate neu co do ho tro > minSupp dua vao itemset confidence
        return filterItemSet;
    }// loai bo cac itemset < minSupp

    //JOIN STEP
    private Set<String> joinItemSets(ItemSets itemSet1, ItemSets itemSet2) {
        Set<String> result = new HashSet<>();
        result.addAll(itemSet1.getItems());
        result.addAll(itemSet2.getItems());
        return result;
    }// ket noi 2 itemset cua L(k) tao 1 itemset moi cua C(k+1)

    private boolean checkItemNotExisted(List<ItemSets> checkList, Set<String> itemCheck) {
        for (ItemSets itemSets : checkList) {
            if (itemSets.getItems().equals(itemCheck))
                return false;
        }
        return true;
    } // kiem tra itemCheck co ton tai trong checkList ko

    //PRUNE STEP
    private boolean isSubSetFrequent(Set<String> itemSet) { // prune step
        List<Set<String>> result = new ArrayList<>();
        for (String s1 : itemSet) {
            Set<String> subList = new HashSet<>();
            for (String s2 : itemSet) {
                if (!s1.equals(s2)) {
                    subList.add(s2);
                }
            }
            result.add(subList);
        }
        for (Set<String> subsItemSet : result) {
            if (calculateSupportItem(subsItemSet) < minSupp) return false; // tap con ko phai la tap pho bien
        }
        return true;
    } // kiem tra xem co tap con nao cua itemCheck ko phai la tap pho bien ko

    private List<ItemSets> genNextCandidate(List<ItemSets> frequent) { // gen C(k+1) tu Lk -- join step
        List<ItemSets> nextCandidate = new ArrayList<>();
        for (ItemSets itemSets1 : frequent) {
            for (ItemSets itemSets2 : frequent) {
                if (itemSets1.getItems().equals(itemSets2.getItems()))
                    continue; // bo qua truong hop 2 itemset nay trung nhau
                ItemSets itemSets = new ItemSets();
                Set<String> items = joinItemSets(itemSets1, itemSets2);
                if (checkItemNotExisted(nextCandidate, items) && isSubSetFrequent(items)) {
                    // kiem tra xem items da ton tai trong nextCandidate va tap con cua item co phai tap pho bien ko
                    itemSets.setItems(items);
                    itemSets.setSupport(calculateSupportItem(items));
                    nextCandidate.add(itemSets);
                }
            }
        }
        return nextCandidate;
    }// tao ra tao ung cu vien C(k+1) tu L(k)

    private Set<String> getItemLeftOver(ItemSets itemSets, Set<String> item) {
        List<String> strings = new ArrayList<>(itemSets.getItems());
        strings.removeAll(item);
        return new HashSet<>(strings);
    }// Lay ra tap hang muc [Y] trong luat [X] => [Y]

    private double calculateConfidence(Set<String> itemSet, Set<String> itemSetX) {
        try {
            return calculateSupportItem(itemSet) / calculateSupportItem(itemSetX);
        } catch (ArithmeticException ex) {
            ex.printStackTrace();
        }
        return 0;
    }// tinh do tin cay cua itemSetX

    private List<ConfidenceItemSet> getListConfidenceItem(List<ItemSets> listItems, Set<String> itemX) { // tim kiem 1 item
        List<ItemSets> listIncludeItem = new ArrayList<>();
        List<ConfidenceItemSet> listConfidenceItemSets = new ArrayList<>();
        for (ItemSets itemSets : listItems) {
            if (itemSets.getItems().containsAll(itemX) && !itemSets.getItems().equals(itemX)) {
                listIncludeItem.add(itemSets);
            }
        } // duyet mang goc tim cac itemset co chua item can tim
        for (ItemSets itemSets : listIncludeItem) {
            ConfidenceItemSet confidenceItemSet = new ConfidenceItemSet();

            confidenceItemSet.setItemSetX(itemX);
            confidenceItemSet.setItemSetY(getItemLeftOver(itemSets, itemX));
            confidenceItemSet.setConfidence(calculateConfidence(itemSets.getItems(), itemX));

            listConfidenceItemSets.add(confidenceItemSet);
        }
        return listConfidenceItemSets;
    } // lay ra danh sach cac itemset va do tin cay cua chung

    private Set<ConfidenceItemSet> filterItemSetConfidence(List<ConfidenceItemSet> confidence) {
        Set<ConfidenceItemSet> filterItemSet = new HashSet<>();
        confidence.forEach(itemSet -> {
            if (itemSet.getConfidence() >= minConf) {
                filterItemSet.add(itemSet);
            }
        }); // loc itemset candidate neu co do ho tro > minSupp dua vao itemset confidence
        return filterItemSet;
    }// loai bo cac itemset co do tin cay < minConf

    private void execute() {
        genFirstCandidate();
        // C(k), L(k)
        List<ItemSets> loopFrequent = filterItemSetSupport(firstCandidate);
        while (!loopFrequent.isEmpty()) { // L(k)
            loopFrequent = genNextCandidate(loopFrequent); // C(k+1)
            loopFrequent = filterItemSetSupport(loopFrequent); // Lk(k+1)
            resultFrequent.addAll(loopFrequent);// get all result here
        }
    } // chay thuat toan Apriori

}
