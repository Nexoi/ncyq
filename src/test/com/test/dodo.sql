--  select ul.uid, ul.nickname, us.like_num, ul.head_icon_url, GROUP_CONCAT(distinct iden.identification_id SEPARATOR ',') identification_ids, img.id, img.height, img.width, img.image_url, img.thumb_image100px_url, img.thumb_image200px_url, img.thumb_image300px_url, img.thumb_image500px_url
--              from ywq_page_person pfg
--              left join ywq_user_login ul on pfg.uid = ul.uid
--              left join ywq_user us on us.uid = ul.uid
--              left join ywq_user_identifications iden on iden.uid = ul.uid
--              left join ywq_photo_wall pw on pw.uid = ul.uid
--              left join ywq_image img on img.id = pw.image_id
--              where pfg.category = 1
--              group by iden.uid
--              order by pfg.order_id

SELECT * from ywq_publish WHERE id /2 =0;