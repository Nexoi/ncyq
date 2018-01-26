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

select v.id, v.category, v.title, v.uid, ul.nickname, ul.head_icon_url, v.view_num, v.create_time,
 img.id as img_id, img.height, img.width, img.image_url, img.thumb_image100px_url, img.thumb_image200px_url, img.thumb_image300px_url, img.thumb_image500px_url,
 vd.id as video_id, vd.cover_url, vd.src_url,  +
 v.diamonds, v.received_diamonds
 from ywq_page_video v join ywq_user_login ul on v.uid = ul.uid
 left join ywq_image img on v.cover_image_id = img.id
 left join ywq_video vd on v.video_id = vd.id
 where v.category = 1