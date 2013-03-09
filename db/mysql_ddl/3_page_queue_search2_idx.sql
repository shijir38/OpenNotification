/*==============================================================*/
/* Database name:  paging                                       */
/* DBMS name:      MySQL 3.23                                   */
/* Created on:     5/22/2004 4:55:14 PM                         */
/*==============================================================*/

use paging;

/*==============================================================*/
/* Index: page_queue_search2_idx                                */
/*==============================================================*/
create index page_queue_search2_idx on page_queue
(
   sender
);