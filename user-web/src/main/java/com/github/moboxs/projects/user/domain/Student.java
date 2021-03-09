package com.github.moboxs.projects.user.domain;

import javax.persistence.*;

// 指定实体类与表名的关系，指定改实体类是一个基于JPA规范的实体类
@Entity
// 指定当前实体类关联的表
@Table(name = "tb_student")
public class Student {

    // 声明属性为一个OID属性
    @Id
    //指定主键生成策略
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //设置属性与数据库字段的关系，如果属性和数据库表名相同可以不设置
    @Column(name = "stu_id")
    private Long stuId;

    @Column(name = "stu_name")
    private String stuName;

    @Column(name = "stu_age")
    private Integer stuAge;

    public Student() {
        super();
    }

    public Long getStuId() {
        return stuId;
    }

    public void setStuId(Long stuId) {
        this.stuId = stuId;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public Integer getStuAge() {
        return stuAge;
    }

    public void setStuAge(Integer stuAge) {
        this.stuAge = stuAge;
    }

    @Override
    public String toString() {
        return "Student{" +
                "stuId=" + stuId +
                ", stuName='" + stuName + '\'' +
                ", stuAge=" + stuAge +
                '}';
    }
}
