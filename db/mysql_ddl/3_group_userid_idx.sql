/*==============================================================*/
/* Database name:  paging                                       */
/* DBMS name:      MySQL 3.23                                   */
/* Created on:     5/22/2004 4:55:14 PM                         */
/*==============================================================*/

use paging;

/*==============================================================*/
/* Index: group_user_id                                         */
/*==============================================================*/
create unique index group_user_id on group_map
(
   group_id,
   user_data_id
);


