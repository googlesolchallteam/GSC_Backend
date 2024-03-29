package com.example.demo.domain.board.service;

import com.example.demo.domain.board.BoardRepository;
import com.example.demo.domain.board.dto.BoardData;
import com.example.demo.domain.board.dto.BoardResponseDTO;
import com.example.demo.domain.board.entity.BoardEntity;
import com.example.demo.domain.member.entity.MemberEntity;
import com.example.demo.domain.member.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    @Transactional
    public void save(String boardData) throws JsonProcessingException {

        System.out.println(boardData);
        ObjectMapper objectMapper = new ObjectMapper();
        BoardData boardData1 = objectMapper.readValue(boardData, BoardData.class);
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setTitle(boardData1.getTitle());
        boardEntity.setContents(boardData1.getContents());
        boardEntity.setPrice(boardData1.getPrice());
        boardEntity.setClothCategory(boardData1.getClothCategory());
        boardEntity.setGender(boardData1.getGender());
        boardEntity.setAddress (boardData1.getAddress());
        boardEntity.setCurrentTime(boardData1.getCurrentTime());

        ObjectMapper objectMapper1 = new ObjectMapper();
        String jsonString = objectMapper1.writeValueAsString(boardData1.getPostImg());
        boardEntity.setPostImg(jsonString);

        String jsonString1 = objectMapper1.writeValueAsString(boardData1.getPosition());
        boardEntity.setPosition(jsonString1);

        Optional<MemberEntity> memberEntityOptional = memberRepository.findById((boardData1.getServerId()));
        MemberEntity memberEntity = memberEntityOptional.get();
        boardEntity.setMemberEntity(memberEntity);
        boardRepository.save(boardEntity);
    }
    public void saveUpdate(String boardData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        BoardData boardData1 = objectMapper.readValue(boardData, BoardData.class);
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardId(boardData1.getBoardId());
        boardEntity.setTitle(boardData1.getTitle());
        boardEntity.setContents(boardData1.getContents());
        boardEntity.setPrice(boardData1.getPrice());
        boardEntity.setClothCategory(boardData1.getClothCategory());
        boardEntity.setGender(boardData1.getGender());
        boardEntity.setAddress (boardData1.getAddress());

        ObjectMapper objectMapper1 = new ObjectMapper();
        String jsonString = objectMapper1.writeValueAsString(boardData1.getPostImg());
        boardEntity.setPostImg(jsonString);

        String jsonString1 = objectMapper1.writeValueAsString(boardData1.getPosition());
        boardEntity.setPosition(jsonString1);

        Optional<MemberEntity> memberEntityOptional = memberRepository.findById((boardData1.getServerId()));
        MemberEntity memberEntity = memberEntityOptional.get();
        boardEntity.setMemberEntity(memberEntity);
        boardRepository.save(boardEntity);

    }
    @Transactional
    public List<BoardResponseDTO> search(String title, String gender, String clothCategory, String address) throws JsonProcessingException {
        List<BoardEntity> boardEntities = boardRepository.findWithParams(address, title, gender, clothCategory);
        List<BoardResponseDTO> boardResponseDTOList = new ArrayList<>();
        for(BoardEntity boardEntity: boardEntities){
            boardResponseDTOList.add(BoardResponseDTO.toDTO(boardEntity));
        }
        return boardResponseDTOList;
    }
    @Transactional
    public List<BoardResponseDTO> findByServerId(Long serverId) throws JsonProcessingException {
        Optional<MemberEntity> memberEntity = memberRepository.findById(serverId);
        List<BoardEntity> boardEntities = memberEntity.get().getBoardEntities();
        List<BoardResponseDTO> boardResponseDTOList = new ArrayList<>();
        for (BoardEntity boardEntity: boardEntities){
            boardResponseDTOList.add(BoardResponseDTO.toDTO(boardEntity));
        }
        return boardResponseDTOList;
    }
    @Transactional
    public BoardResponseDTO findByBoardId(Long boardId) throws JsonProcessingException {
        Optional<BoardEntity> boardEntityOptional = boardRepository.findById(boardId);
        if (boardEntityOptional.isPresent()){
            BoardEntity boardEntity = boardEntityOptional.get();
            BoardResponseDTO boardResponseDTO = BoardResponseDTO.toDTO(boardEntityOptional.get());
            boardResponseDTO.setNickname(boardEntity.getMemberEntity().getNickname());
            boardResponseDTO.setProfileImage(boardEntity.getMemberEntity().getProfileImage());
            return boardResponseDTO;
        }
        else {
            return null; // 또는 다른 상태 코드를 사용
        }

    }
    public void delete(Long boardId) {
        boardRepository.deleteById(boardId);
    }

    public List<BoardResponseDTO> findAll() throws JsonProcessingException {
         List<BoardEntity> boardEntities = boardRepository.findAll();
         List<BoardResponseDTO> boardResponseDTOList = new ArrayList<>();
         for (BoardEntity boardEntity: boardEntities) boardResponseDTOList.add(BoardResponseDTO.toDTO(boardEntity));
         return boardResponseDTOList;
    }
}
