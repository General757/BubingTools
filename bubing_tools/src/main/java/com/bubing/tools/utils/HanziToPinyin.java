//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.bubing.tools.utils;

import android.text.TextUtils;
import android.util.Log;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Locale;

/**
 * @ClassName: HanziToPinyin
 * @Author: Bubing
 * @Date: 2020/9/9 3:24 PM
 * @Description: 汉字转拼音
 */
public class HanziToPinyin {
    private static final boolean DEBUG = true;
    private static final String TAG = "HanziToPinyin";
    private static final char[] UNIHANS = new char[]{'吖', '哎', '安', '肮', '凹', '八', '掰', '扳', '邦', '勹', '陂', '奔', '伻', '皀', '砭', '灬', '憋', '汃', '冫', '癶', '峬', '嚓', '偲', '参', '仓', '撡', '冊', '嵾', '噌', '扠', '拆', '辿', '伥', '抄', '车', '抻', '阷', '吃', '充', '抽', '出', '搋', '巛', '刅', '吹', '旾', '踔', '呲', '从', '凑', '粗', '汆', '崔', '邨', '搓', '咑', '呆', '丹', '当', '刀', '恴', '扥', '灯', '仾', '嗲', '敁', '刁', '爹', '丁', '丟', '东', '吺', '剢', '耑', '垖', '吨', '多', '妸', '奀', '鞥', '而', '发', '帆', '匚', '飞', '分', '丰', '覅', '仏', '垺', '紑', '夫', '猤', '旮', '侅', '干', '冈', '皋', '戈', '给', '根', '揯', '喼', '嗰', '工', '勾', '估', '鸹', '乖', '关', '光', '归', '丨', '謴', '呙', '妎', '咍', '佄', '夯', '茠', '诃', '黒', '拫', '亨', '叿', '齁', '乎', '花', '怀', '欢', '巟', '灰', '昏', '吙', '丌', '加', '戋', '江', '艽', '阶', '巾', '坕', '冂', '丩', '凥', '姢', '噘', '军', '咔', '开', '鎎', '忼', '尻', '匼', '肎', '劥', '空', '抠', '扝', '夸', '蒯', '宽', '匡', '亏', '坤', '扩', '拉', '來', '兰', '啷', '捞', '仂', '雷', '脷', '棱', '楞', '唎', '俩', '嫾', '良', '蹽', '埓', '厸', '拎', '溜', '龙', '娄', '噜', '孪', '抡', '頱', '妈', '埋', '颟', '牤', '猫', '呅', '门', '氓', '咪', '宀', '喵', '乜', '民', '名', '谬', '摸', '牟', '母', '拏', '腉', '囡', '囔', '孬', '讷', '娞', '嫩', '能', '銰', '拈', '娘', '鸟', '捏', '囜', '宁', '妞', '农', '羺', '奴', '奻', '黁', '郍', '噢', '讴', '妑', '拍', '眅', '汸', '抛', '呸', '喷', '匉', '乶', '片', '剽', '氕', '姘', '乒', '钋', '剖', '仆', '七', '掐', '千', '呛', '悄', '切', '亲', '靑', '宆', '瓗', '区', '峑', '炔', '夋', '呥', '穣', '荛', '惹', '人', '扔', '日', '戎', '厹', '邚', '堧', '桵', '闰', '挼', '仨', '毢', '三', '桒', '掻', '色', '森', '僧', '杀', '筛', '山', '伤', '弰', '奢', '申', '升', '尸', '収', '书', '刷', '衰', '闩', '双', '谁', '吮', '说', '厶', '忪', '凁', '苏', '狻', '夊', '孙', '唆', '他', '孡', '坍', '汤', '夲', '忑', '膯', '剔', '天', '旫', '怗', '厅', '炵', '偷', '凸', '湍', '推', '吞', '讬', '劸', '歪', '弯', '尣', '危', '昷', '翁', '挝', '乌', '夕', '呷', '仙', '乡', '灱', '些', '心', '兴', '凶', '休', '戌', '吅', '疶', '瀥', '丫', '咽', '央', '幺', '倻', '一', '乚', '应', '唷', '佣', '优', '纡', '囦', '曰', '蒀', '帀', '災', '兂', '牂', '傮', '啫', '鱡', '怎', '曽', '吒', '捚', '沾', '张', '佋', '蜇', '贞', '黮', '之', '中', '州', '朱', '抓', '拽', '专', '妆', '隹', '宒', '卓', '仔', '宗', '邹', '租', '劗', '嗺', '尊', '昨'};
    private static final byte[][] PINYINS = new byte[][]{{65, 0, 0, 0, 0, 0}, {65, 73, 0, 0, 0, 0}, {65, 78, 0, 0, 0, 0}, {65, 78, 71, 0, 0, 0}, {65, 79, 0, 0, 0, 0}, {66, 65, 0, 0, 0, 0}, {66, 65, 73, 0, 0, 0}, {66, 65, 78, 0, 0, 0}, {66, 65, 78, 71, 0, 0}, {66, 65, 79, 0, 0, 0}, {66, 69, 73, 0, 0, 0}, {66, 69, 78, 0, 0, 0}, {66, 69, 78, 71, 0, 0}, {66, 73, 0, 0, 0, 0}, {66, 73, 65, 78, 0, 0}, {66, 73, 65, 79, 0, 0}, {66, 73, 69, 0, 0, 0}, {66, 73, 78, 0, 0, 0}, {66, 73, 78, 71, 0, 0}, {66, 79, 0, 0, 0, 0}, {66, 85, 0, 0, 0, 0}, {67, 65, 0, 0, 0, 0}, {67, 65, 73, 0, 0, 0}, {67, 65, 78, 0, 0, 0}, {67, 65, 78, 71, 0, 0}, {67, 65, 79, 0, 0, 0}, {67, 69, 0, 0, 0, 0}, {67, 69, 78, 0, 0, 0}, {67, 69, 78, 71, 0, 0}, {67, 72, 65, 0, 0, 0}, {67, 72, 65, 73, 0, 0}, {67, 72, 65, 78, 0, 0}, {67, 72, 65, 78, 71, 0}, {67, 72, 65, 79, 0, 0}, {67, 72, 69, 0, 0, 0}, {67, 72, 69, 78, 0, 0}, {67, 72, 69, 78, 71, 0}, {67, 72, 73, 0, 0, 0}, {67, 72, 79, 78, 71, 0}, {67, 72, 79, 85, 0, 0}, {67, 72, 85, 0, 0, 0}, {67, 72, 85, 65, 73, 0}, {67, 72, 85, 65, 78, 0}, {67, 72, 85, 65, 78, 71}, {67, 72, 85, 73, 0, 0}, {67, 72, 85, 78, 0, 0}, {67, 72, 85, 79, 0, 0}, {67, 73, 0, 0, 0, 0}, {67, 79, 78, 71, 0, 0}, {67, 79, 85, 0, 0, 0}, {67, 85, 0, 0, 0, 0}, {67, 85, 65, 78, 0, 0}, {67, 85, 73, 0, 0, 0}, {67, 85, 78, 0, 0, 0}, {67, 85, 79, 0, 0, 0}, {68, 65, 0, 0, 0, 0}, {68, 65, 73, 0, 0, 0}, {68, 65, 78, 0, 0, 0}, {68, 65, 78, 71, 0, 0}, {68, 65, 79, 0, 0, 0}, {68, 69, 0, 0, 0, 0}, {68, 69, 78, 0, 0, 0}, {68, 69, 78, 71, 0, 0}, {68, 73, 0, 0, 0, 0}, {68, 73, 65, 0, 0, 0}, {68, 73, 65, 78, 0, 0}, {68, 73, 65, 79, 0, 0}, {68, 73, 69, 0, 0, 0}, {68, 73, 78, 71, 0, 0}, {68, 73, 85, 0, 0, 0}, {68, 79, 78, 71, 0, 0}, {68, 79, 85, 0, 0, 0}, {68, 85, 0, 0, 0, 0}, {68, 85, 65, 78, 0, 0}, {68, 85, 73, 0, 0, 0}, {68, 85, 78, 0, 0, 0}, {68, 85, 79, 0, 0, 0}, {69, 0, 0, 0, 0, 0}, {69, 78, 0, 0, 0, 0}, {69, 78, 71, 0, 0, 0}, {69, 82, 0, 0, 0, 0}, {70, 65, 0, 0, 0, 0}, {70, 65, 78, 0, 0, 0}, {70, 65, 78, 71, 0, 0}, {70, 69, 73, 0, 0, 0}, {70, 69, 78, 0, 0, 0}, {70, 69, 78, 71, 0, 0}, {70, 73, 65, 79, 0, 0}, {70, 79, 0, 0, 0, 0}, {70, 85, 0, 0, 0, 0}, {70, 79, 85, 0, 0, 0}, {70, 85, 0, 0, 0, 0}, {71, 85, 73, 0, 0, 0}, {71, 65, 0, 0, 0, 0}, {71, 65, 73, 0, 0, 0}, {71, 65, 78, 0, 0, 0}, {71, 65, 78, 71, 0, 0}, {71, 65, 79, 0, 0, 0}, {71, 69, 0, 0, 0, 0}, {71, 69, 73, 0, 0, 0}, {71, 69, 78, 0, 0, 0}, {71, 69, 78, 71, 0, 0}, {74, 73, 69, 0, 0, 0}, {71, 69, 0, 0, 0, 0}, {71, 79, 78, 71, 0, 0}, {71, 79, 85, 0, 0, 0}, {71, 85, 0, 0, 0, 0}, {71, 85, 65, 0, 0, 0}, {71, 85, 65, 73, 0, 0}, {71, 85, 65, 78, 0, 0}, {71, 85, 65, 78, 71, 0}, {71, 85, 73, 0, 0, 0}, {71, 85, 78, 0, 0, 0}, {71, 85, 65, 78, 0, 0}, {71, 85, 79, 0, 0, 0}, {72, 65, 0, 0, 0, 0}, {72, 65, 73, 0, 0, 0}, {72, 65, 78, 0, 0, 0}, {72, 65, 78, 71, 0, 0}, {72, 65, 79, 0, 0, 0}, {72, 69, 0, 0, 0, 0}, {72, 69, 73, 0, 0, 0}, {72, 69, 78, 0, 0, 0}, {72, 69, 78, 71, 0, 0}, {72, 79, 78, 71, 0, 0}, {72, 79, 85, 0, 0, 0}, {72, 85, 0, 0, 0, 0}, {72, 85, 65, 0, 0, 0}, {72, 85, 65, 73, 0, 0}, {72, 85, 65, 78, 0, 0}, {72, 85, 65, 78, 71, 0}, {72, 85, 73, 0, 0, 0}, {72, 85, 78, 0, 0, 0}, {72, 85, 79, 0, 0, 0}, {74, 73, 0, 0, 0, 0}, {74, 73, 65, 0, 0, 0}, {74, 73, 65, 78, 0, 0}, {74, 73, 65, 78, 71, 0}, {74, 73, 65, 79, 0, 0}, {74, 73, 69, 0, 0, 0}, {74, 73, 78, 0, 0, 0}, {74, 73, 78, 71, 0, 0}, {74, 73, 79, 78, 71, 0}, {74, 73, 85, 0, 0, 0}, {74, 85, 0, 0, 0, 0}, {74, 85, 65, 78, 0, 0}, {74, 85, 69, 0, 0, 0}, {74, 85, 78, 0, 0, 0}, {75, 65, 0, 0, 0, 0}, {75, 65, 73, 0, 0, 0}, {75, 65, 78, 0, 0, 0}, {75, 65, 78, 71, 0, 0}, {75, 65, 79, 0, 0, 0}, {75, 69, 0, 0, 0, 0}, {75, 69, 78, 0, 0, 0}, {75, 69, 78, 71, 0, 0}, {75, 79, 78, 71, 0, 0}, {75, 79, 85, 0, 0, 0}, {75, 85, 0, 0, 0, 0}, {75, 85, 65, 0, 0, 0}, {75, 85, 65, 73, 0, 0}, {75, 85, 65, 78, 0, 0}, {75, 85, 65, 78, 71, 0}, {75, 85, 73, 0, 0, 0}, {75, 85, 78, 0, 0, 0}, {75, 85, 79, 0, 0, 0}, {76, 65, 0, 0, 0, 0}, {76, 65, 73, 0, 0, 0}, {76, 65, 78, 0, 0, 0}, {76, 65, 78, 71, 0, 0}, {76, 65, 79, 0, 0, 0}, {76, 69, 0, 0, 0, 0}, {76, 69, 73, 0, 0, 0}, {76, 73, 0, 0, 0, 0}, {76, 73, 78, 71, 0, 0}, {76, 69, 78, 71, 0, 0}, {76, 73, 0, 0, 0, 0}, {76, 73, 65, 0, 0, 0}, {76, 73, 65, 78, 0, 0}, {76, 73, 65, 78, 71, 0}, {76, 73, 65, 79, 0, 0}, {76, 73, 69, 0, 0, 0}, {76, 73, 78, 0, 0, 0}, {76, 73, 78, 71, 0, 0}, {76, 73, 85, 0, 0, 0}, {76, 79, 78, 71, 0, 0}, {76, 79, 85, 0, 0, 0}, {76, 85, 0, 0, 0, 0}, {76, 85, 65, 78, 0, 0}, {76, 85, 78, 0, 0, 0}, {76, 85, 79, 0, 0, 0}, {77, 65, 0, 0, 0, 0}, {77, 65, 73, 0, 0, 0}, {77, 65, 78, 0, 0, 0}, {77, 65, 78, 71, 0, 0}, {77, 65, 79, 0, 0, 0}, {77, 69, 73, 0, 0, 0}, {77, 69, 78, 0, 0, 0}, {77, 69, 78, 71, 0, 0}, {77, 73, 0, 0, 0, 0}, {77, 73, 65, 78, 0, 0}, {77, 73, 65, 79, 0, 0}, {77, 73, 69, 0, 0, 0}, {77, 73, 78, 0, 0, 0}, {77, 73, 78, 71, 0, 0}, {77, 73, 85, 0, 0, 0}, {77, 79, 0, 0, 0, 0}, {77, 79, 85, 0, 0, 0}, {77, 85, 0, 0, 0, 0}, {78, 65, 0, 0, 0, 0}, {78, 65, 73, 0, 0, 0}, {78, 65, 78, 0, 0, 0}, {78, 65, 78, 71, 0, 0}, {78, 65, 79, 0, 0, 0}, {78, 69, 0, 0, 0, 0}, {78, 69, 73, 0, 0, 0}, {78, 69, 78, 0, 0, 0}, {78, 69, 78, 71, 0, 0}, {78, 73, 0, 0, 0, 0}, {78, 73, 65, 78, 0, 0}, {78, 73, 65, 78, 71, 0}, {78, 73, 65, 79, 0, 0}, {78, 73, 69, 0, 0, 0}, {78, 73, 78, 0, 0, 0}, {78, 73, 78, 71, 0, 0}, {78, 73, 85, 0, 0, 0}, {78, 79, 78, 71, 0, 0}, {78, 79, 85, 0, 0, 0}, {78, 85, 0, 0, 0, 0}, {78, 85, 65, 78, 0, 0}, {78, 85, 78, 0, 0, 0}, {78, 85, 79, 0, 0, 0}, {79, 0, 0, 0, 0, 0}, {79, 85, 0, 0, 0, 0}, {80, 65, 0, 0, 0, 0}, {80, 65, 73, 0, 0, 0}, {80, 65, 78, 0, 0, 0}, {80, 65, 78, 71, 0, 0}, {80, 65, 79, 0, 0, 0}, {80, 69, 73, 0, 0, 0}, {80, 69, 78, 0, 0, 0}, {80, 69, 78, 71, 0, 0}, {80, 73, 0, 0, 0, 0}, {80, 73, 65, 78, 0, 0}, {80, 73, 65, 79, 0, 0}, {80, 73, 69, 0, 0, 0}, {80, 73, 78, 0, 0, 0}, {80, 73, 78, 71, 0, 0}, {80, 79, 0, 0, 0, 0}, {80, 79, 85, 0, 0, 0}, {80, 85, 0, 0, 0, 0}, {81, 73, 0, 0, 0, 0}, {81, 73, 65, 0, 0, 0}, {81, 73, 65, 78, 0, 0}, {81, 73, 65, 78, 71, 0}, {81, 73, 65, 79, 0, 0}, {81, 73, 69, 0, 0, 0}, {81, 73, 78, 0, 0, 0}, {81, 73, 78, 71, 0, 0}, {81, 73, 79, 78, 71, 0}, {81, 73, 85, 0, 0, 0}, {81, 85, 0, 0, 0, 0}, {81, 85, 65, 78, 0, 0}, {81, 85, 69, 0, 0, 0}, {81, 85, 78, 0, 0, 0}, {82, 65, 78, 0, 0, 0}, {82, 65, 78, 71, 0, 0}, {82, 65, 79, 0, 0, 0}, {82, 69, 0, 0, 0, 0}, {82, 69, 78, 0, 0, 0}, {82, 69, 78, 71, 0, 0}, {82, 73, 0, 0, 0, 0}, {82, 79, 78, 71, 0, 0}, {82, 79, 85, 0, 0, 0}, {82, 85, 0, 0, 0, 0}, {82, 85, 65, 78, 0, 0}, {82, 85, 73, 0, 0, 0}, {82, 85, 78, 0, 0, 0}, {82, 85, 79, 0, 0, 0}, {83, 65, 0, 0, 0, 0}, {83, 65, 73, 0, 0, 0}, {83, 65, 78, 0, 0, 0}, {83, 65, 78, 71, 0, 0}, {83, 65, 79, 0, 0, 0}, {83, 69, 0, 0, 0, 0}, {83, 69, 78, 0, 0, 0}, {83, 69, 78, 71, 0, 0}, {83, 72, 65, 0, 0, 0}, {83, 72, 65, 73, 0, 0}, {83, 72, 65, 78, 0, 0}, {83, 72, 65, 78, 71, 0}, {83, 72, 65, 79, 0, 0}, {83, 72, 69, 0, 0, 0}, {83, 72, 69, 78, 0, 0}, {83, 72, 69, 78, 71, 0}, {83, 72, 73, 0, 0, 0}, {83, 72, 79, 85, 0, 0}, {83, 72, 85, 0, 0, 0}, {83, 72, 85, 65, 0, 0}, {83, 72, 85, 65, 73, 0}, {83, 72, 85, 65, 78, 0}, {83, 72, 85, 65, 78, 71}, {83, 72, 85, 73, 0, 0}, {83, 72, 85, 78, 0, 0}, {83, 72, 85, 79, 0, 0}, {83, 73, 0, 0, 0, 0}, {83, 79, 78, 71, 0, 0}, {83, 79, 85, 0, 0, 0}, {83, 85, 0, 0, 0, 0}, {83, 85, 65, 78, 0, 0}, {83, 85, 73, 0, 0, 0}, {83, 85, 78, 0, 0, 0}, {83, 85, 79, 0, 0, 0}, {84, 65, 0, 0, 0, 0}, {84, 65, 73, 0, 0, 0}, {84, 65, 78, 0, 0, 0}, {84, 65, 78, 71, 0, 0}, {84, 65, 79, 0, 0, 0}, {84, 69, 0, 0, 0, 0}, {84, 69, 78, 71, 0, 0}, {84, 73, 0, 0, 0, 0}, {84, 73, 65, 78, 0, 0}, {84, 73, 65, 79, 0, 0}, {84, 73, 69, 0, 0, 0}, {84, 73, 78, 71, 0, 0}, {84, 79, 78, 71, 0, 0}, {84, 79, 85, 0, 0, 0}, {84, 85, 0, 0, 0, 0}, {84, 85, 65, 78, 0, 0}, {84, 85, 73, 0, 0, 0}, {84, 85, 78, 0, 0, 0}, {84, 85, 79, 0, 0, 0}, {87, 65, 0, 0, 0, 0}, {87, 65, 73, 0, 0, 0}, {87, 65, 78, 0, 0, 0}, {87, 65, 78, 71, 0, 0}, {87, 69, 73, 0, 0, 0}, {87, 69, 78, 0, 0, 0}, {87, 69, 78, 71, 0, 0}, {87, 79, 0, 0, 0, 0}, {87, 85, 0, 0, 0, 0}, {88, 73, 0, 0, 0, 0}, {88, 73, 65, 0, 0, 0}, {88, 73, 65, 78, 0, 0}, {88, 73, 65, 78, 71, 0}, {88, 73, 65, 79, 0, 0}, {88, 73, 69, 0, 0, 0}, {88, 73, 78, 0, 0, 0}, {88, 73, 78, 71, 0, 0}, {88, 73, 79, 78, 71, 0}, {88, 73, 85, 0, 0, 0}, {88, 85, 0, 0, 0, 0}, {88, 85, 65, 78, 0, 0}, {88, 85, 69, 0, 0, 0}, {88, 85, 78, 0, 0, 0}, {89, 65, 0, 0, 0, 0}, {89, 65, 78, 0, 0, 0}, {89, 65, 78, 71, 0, 0}, {89, 65, 79, 0, 0, 0}, {89, 69, 0, 0, 0, 0}, {89, 73, 0, 0, 0, 0}, {89, 73, 78, 0, 0, 0}, {89, 73, 78, 71, 0, 0}, {89, 79, 0, 0, 0, 0}, {89, 79, 78, 71, 0, 0}, {89, 79, 85, 0, 0, 0}, {89, 85, 0, 0, 0, 0}, {89, 85, 65, 78, 0, 0}, {89, 85, 69, 0, 0, 0}, {89, 85, 78, 0, 0, 0}, {90, 65, 0, 0, 0, 0}, {90, 65, 73, 0, 0, 0}, {90, 65, 78, 0, 0, 0}, {90, 65, 78, 71, 0, 0}, {90, 65, 79, 0, 0, 0}, {90, 69, 0, 0, 0, 0}, {90, 69, 73, 0, 0, 0}, {90, 69, 78, 0, 0, 0}, {90, 69, 78, 71, 0, 0}, {90, 72, 65, 0, 0, 0}, {90, 72, 65, 73, 0, 0}, {90, 72, 65, 78, 0, 0}, {90, 72, 65, 78, 71, 0}, {90, 72, 65, 79, 0, 0}, {90, 72, 69, 0, 0, 0}, {90, 72, 69, 78, 0, 0}, {90, 72, 69, 78, 71, 0}, {90, 72, 73, 0, 0, 0}, {90, 72, 79, 78, 71, 0}, {90, 72, 79, 85, 0, 0}, {90, 72, 85, 0, 0, 0}, {90, 72, 85, 65, 0, 0}, {90, 72, 85, 65, 73, 0}, {90, 72, 85, 65, 78, 0}, {90, 72, 85, 65, 78, 71}, {90, 72, 85, 73, 0, 0}, {90, 72, 85, 78, 0, 0}, {90, 72, 85, 79, 0, 0}, {90, 73, 0, 0, 0, 0}, {90, 79, 78, 71, 0, 0}, {90, 79, 85, 0, 0, 0}, {90, 85, 0, 0, 0, 0}, {90, 85, 65, 78, 0, 0}, {90, 85, 73, 0, 0, 0}, {90, 85, 78, 0, 0, 0}, {90, 85, 79, 0, 0, 0}};
    private static final String FIRST_PINYIN_UNIHAN = "吖";
    private static final String LAST_PINYIN_UNIHAN = "咗";
    private static final char FIRST_UNIHAN = '㐀';
    private static final Collator COLLATOR;
    private static HanziToPinyin sInstance;
    private final boolean mHasChinaCollator;

    protected HanziToPinyin(boolean hasChinaCollator) {
        this.mHasChinaCollator = hasChinaCollator;
    }

    public static HanziToPinyin getInstance() {
        Class var0 = HanziToPinyin.class;
        synchronized (HanziToPinyin.class) {
            if (sInstance != null) {
                return sInstance;
            } else {
                Locale[] locale = Collator.getAvailableLocales();

                for (int i = 0; i < locale.length; ++i) {
                    if (locale[i].equals(Locale.CHINA)) {
                        sInstance = new HanziToPinyin(true);
                        return sInstance;
                    }
                }

                Log.w("HanziToPinyin", "There is no Chinese collator, HanziToPinyin is disabled");
                sInstance = new HanziToPinyin(false);
                return sInstance;
            }
        }
    }

    private HanziToPinyin.Token getToken(char character) {
        HanziToPinyin.Token token = new HanziToPinyin.Token();
        String letter = Character.toString(character);
        token.source = letter;
        int offset = -1;
        if (character < 256) {
            token.type = 1;
            token.target = letter;
            return token;
        } else if (character < 13312) {
            token.type = 3;
            token.target = letter;
            return token;
        } else {
            int cmp = COLLATOR.compare(letter, "吖");
            if (cmp < 0) {
                token.type = 3;
                token.target = letter;
                return token;
            } else {
                if (cmp == 0) {
                    token.type = 2;
                    offset = 0;
                } else {
                    cmp = COLLATOR.compare(letter, "咗");
                    if (cmp > 0) {
                        token.type = 3;
                        token.target = letter;
                        return token;
                    }

                    if (cmp == 0) {
                        token.type = 2;
                        offset = UNIHANS.length - 1;
                    }
                }

                token.type = 2;
                int j;
                if (offset < 0) {
                    int begin = 0;
                    j = UNIHANS.length - 1;

                    while (begin <= j) {
                        offset = (begin + j) / 2;
                        String unihan = Character.toString(UNIHANS[offset]);
                        cmp = COLLATOR.compare(letter, unihan);
                        if (cmp == 0) {
                            break;
                        }

                        if (cmp > 0) {
                            begin = offset + 1;
                        } else {
                            j = offset - 1;
                        }
                    }
                }

                if (cmp < 0) {
                    --offset;
                }

                StringBuilder pinyin = new StringBuilder();

                for (j = 0; j < PINYINS[offset].length && PINYINS[offset][j] != 0; ++j) {
                    pinyin.append((char) PINYINS[offset][j]);
                }

                token.target = pinyin.toString();
                return token;
            }
        }
    }

    public ArrayList<HanziToPinyin.Token> get(String input) {
        ArrayList<HanziToPinyin.Token> tokens = new ArrayList();
        if (this.mHasChinaCollator && !TextUtils.isEmpty(input)) {
            int inputLength = input.length();
            StringBuilder sb = new StringBuilder();
            int tokenType = 1;

            for (int i = 0; i < inputLength; ++i) {
                char character = input.charAt(i);
                if (character == ' ') {
                    if (sb.length() > 0) {
                        this.addToken(sb, tokens, tokenType);
                    }
                } else if (character < 256) {
                    if (tokenType != 1 && sb.length() > 0) {
                        this.addToken(sb, tokens, tokenType);
                    }

                    tokenType = 1;
                    sb.append(character);
                } else if (character < 13312) {
                    if (tokenType != 3 && sb.length() > 0) {
                        this.addToken(sb, tokens, tokenType);
                    }

                    tokenType = 3;
                    sb.append(character);
                } else {
                    HanziToPinyin.Token t = this.getToken(character);
                    if (t.type == 2) {
                        if (sb.length() > 0) {
                            this.addToken(sb, tokens, tokenType);
                        }

                        tokens.add(t);
                        tokenType = 2;
                    } else {
                        if (tokenType != t.type && sb.length() > 0) {
                            this.addToken(sb, tokens, tokenType);
                        }

                        tokenType = t.type;
                        sb.append(character);
                    }
                }
            }

            if (sb.length() > 0) {
                this.addToken(sb, tokens, tokenType);
            }

            return tokens;
        } else {
            return tokens;
        }
    }

    private void addToken(StringBuilder sb, ArrayList<HanziToPinyin.Token> tokens, int tokenType) {
        String str = sb.toString();
        tokens.add(new HanziToPinyin.Token(tokenType, str, str));
        sb.setLength(0);
    }

    static {
        COLLATOR = Collator.getInstance(Locale.CHINA);
    }

    public static class Token {
        public static final String SEPARATOR = " ";
        public static final int LATIN = 1;
        public static final int PINYIN = 2;
        public static final int UNKNOWN = 3;
        public int type;
        public String source;
        public String target;

        public Token() {
        }

        public Token(int type, String source, String target) {
            this.type = type;
            this.source = source;
            this.target = target;
        }
    }
}
